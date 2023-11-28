package KryptoTrading.GUI.view;

import KryptoTrading.Datenhaltung.User;
import KryptoTrading.Datenhaltung.Wallet;
import KryptoTrading.GUI.model.Globals;
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

public class AddWalletView  extends Stage {
    AdminLayout adminLayout;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final String TITLE = "Wallet Hinzufügen";
    public AddWalletView(AdminLayout adminLayout) {
        this.adminLayout = adminLayout;
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(Main.mainStage);
        this.setScene(new Scene(new AddWalletView.AddWalletLayout(), WIDTH, HEIGHT));
        this.setTitle(TITLE);
    }

    public class AddWalletLayout extends BorderPane {

        public AddWalletLayout() {
            VBox centerBox = new VBox();
            centerBox.setAlignment(Pos.CENTER);

            HBox buttonBox = new HBox();
            buttonBox.setSpacing(Globals.DEFAULT_SPACING);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(5,5,5,5));



            GridPane gp = new GridPane();
            gp.setAlignment(Pos.CENTER);
            gp.setHgap(Globals.DEFAULT_SPACING);
            gp.setVgap(Globals.DEFAULT_SPACING);

            Label usernameLabel = new Label("Name des Benutzers");
            Label identifierNameLabel = new Label("Name der Wallet");

            TextField usernameTextField = new TextField();
            TextField identifierNameTextField = new TextField();

            gp.add(usernameLabel, 0, 0);
            gp.add(identifierNameLabel, 0, 1);

            gp.add(usernameTextField, 1, 0);
            gp.add(identifierNameTextField, 1, 1);

            Label infoLabel = new Label();
            infoLabel.setAlignment(Pos.BOTTOM_CENTER);

            Button readyButton = new Button("OK");
            readyButton.setOnAction(e -> {
                User b = User.getUser(usernameTextField.getText());
                Wallet w = new Wallet();
                w.add_wallet(b, identifierNameTextField.getText());
                adminLayout.refreshTable();
                AddWalletView.this.close();

            });

            Button quitButton = new Button("Abbrechen");
            quitButton.setOnAction(e -> {

                AddWalletView.this.close();
            });


            centerBox.getChildren().addAll(gp);
            buttonBox.getChildren().addAll(readyButton, quitButton);

            VBox bottomBox = new VBox();
            bottomBox.setAlignment(Pos.CENTER);
            bottomBox.getChildren().addAll(infoLabel, buttonBox);


            this.setCenter(centerBox);
            this.setBottom(bottomBox);
        }
    }
}
