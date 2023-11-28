package KryptoTrading.GUI.view;

import KryptoTrading.Datenhaltung.User;
import KryptoTrading.Datenhaltung.Wallet;
import KryptoTrading.GUI.model.Globals;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddEntryView extends Stage {
	public static final int WIDTH = 300;
	public static final int HEIGHT = 200;
	public static final String TITLE = "Wallet Hinzuf�gen";

	String username;
	
	
	public AddEntryView(String username) {
		this.username = username;
		this.setResizable(false);
		this.initModality(Modality.APPLICATION_MODAL);
		this.initOwner(Main.mainStage);
		this.setScene(new Scene(new AddEntryLayout(), WIDTH, HEIGHT));
		this.setTitle(TITLE);
	}
	
	
	
	
	
	
	public class AddEntryLayout extends BorderPane {
		
		
		public AddEntryLayout() {
			VBox centerBox = new VBox();
			centerBox.setAlignment(Pos.CENTER);


			GridPane gp = new GridPane();
			gp.setAlignment(Pos.CENTER);
			gp.setHgap(Globals.DEFAULT_SPACING);
			gp.setVgap(Globals.DEFAULT_SPACING);

			Label nameLabel = new Label("Name");

			TextField nameTextField = new TextField();

			gp.add(nameLabel, 0, 0);
			gp.add(nameTextField, 1, 0);
			
			HBox buttonBox = new HBox();
			buttonBox.setSpacing(Globals.DEFAULT_SPACING);
			buttonBox.setAlignment(Pos.CENTER);
			buttonBox.setPadding(new Insets(5,5,5,5));
			
			Button readyButton = new Button("OK");
			readyButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (Main.mainStage.getScene().getRoot() instanceof HomeLayout) {
						HomeLayout hl = (HomeLayout)Main.mainStage.getScene().getRoot();
						try {
							
							Wallet w = new Wallet();
							w.add_wallet(User.getUser(username), nameTextField.getText());
							w = Wallet.getWallet(User.getUser(username), nameTextField.getText());
							hl.addEntry(w);
							AddEntryView.this.close();
						} catch (Exception e) {
							AddEntryView.this.close();
							e.printStackTrace();
						}
						
					}
					else {
						//TODO: exception handling
					}
					
					
				}
			});
			
			Button cancelButton = new Button("Abbrechen");
			cancelButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					AddEntryView.this.close();
				}
			});
			
			buttonBox.getChildren().addAll(readyButton, cancelButton);
			centerBox.getChildren().add(gp);
			
			this.setCenter(centerBox);
			this.setBottom(buttonBox);
			
		}
		
	}

}
