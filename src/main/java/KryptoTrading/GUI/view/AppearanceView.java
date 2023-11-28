package KryptoTrading.GUI.view;

import KryptoTrading.GUI.model.Globals;
import KryptoTrading.GUI.model.Util;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventListener;

public class AppearanceView extends Stage{
	public static final int WIDTH = 300;
	public static final int HEIGHT = 300;
	
	public AppearanceView() {
		this.initModality(Modality.APPLICATION_MODAL);
		this.initOwner(Main.mainStage);
		this.setScene(new Scene(new AppearanceLayout(), WIDTH, HEIGHT));
		
	}
	
	
	
	
	
	public class AppearanceLayout extends BorderPane {
		
		public AppearanceLayout() {
			VBox centerBox = new VBox();
			centerBox.setAlignment(Pos.CENTER);
			centerBox.setSpacing(Globals.DEFAULT_SPACING);
			centerBox.setPadding(new Insets(5,5,5,5));
			
			CheckBox darkModeCheckBox = new CheckBox("Darkmode");
			darkModeCheckBox.setSelected(Main.config.isDarkMode());

			centerBox.getChildren().addAll(darkModeCheckBox);
			
			HBox bottomBox = new HBox();
			bottomBox.setAlignment(Pos.CENTER);
			bottomBox.setSpacing(Globals.DEFAULT_SPACING);
			bottomBox.setPadding(new Insets(5,5,5,5));
			
			Button saveAndCloseButton = new Button("Speichern und Schließen");
			saveAndCloseButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Main.config.setDarkMode(darkModeCheckBox.isSelected());
					try {
						Util.saveConfig(Main.config);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					AppearanceView.this.close();
				}
			});

			Button cancelButton = new Button("Abbrechen");
			cancelButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					AppearanceView.this.close();
				}
			});
			
			bottomBox.getChildren().addAll(saveAndCloseButton, cancelButton);
			
			this.setCenter(centerBox);
			this.setBottom(bottomBox);
			
		}
	}

}
