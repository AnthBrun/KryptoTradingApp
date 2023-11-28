package KryptoTrading.GUI.view;

import KryptoTrading.Fachlogik.UserFunctions;
import KryptoTrading.GUI.model.Globals;
import KryptoTrading.GUI.model.exceptions.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddUserView extends Stage {
    AdminLayout adminLayout;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final String TITLE = "User Hinzufügen";
    public AddUserView(AdminLayout adminLayout) {
        this.adminLayout = adminLayout;
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(Main.mainStage);
        this.setScene(new Scene(new AddUserView.AddUserLayout(), WIDTH, HEIGHT));
        this.setTitle(TITLE);
    }


    public class AddUserLayout extends BorderPane {

        public AddUserLayout() {
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

            Label nameLabel = new Label("Username");
            Label passwordLabel = new Label("Passwort");

            TextField nameTextField = new TextField();
            PasswordField passwordField = new PasswordField();

            gp.add(nameLabel, 0, 0);
            gp.add(passwordLabel, 0, 1);

            gp.add(nameTextField, 1, 0);
            gp.add(passwordField, 1, 1);

            Label infoLabel = new Label();
            infoLabel.setAlignment(Pos.BOTTOM_CENTER);

            Button readyButton = new Button("OK");
            readyButton.setOnAction(e -> {


                try {
                    UserFunctions.register(nameTextField.getText(), passwordField.getText(), passwordField.getText());
                    adminLayout.refreshTable();
                    AddUserView.this.close();
                } catch (PasswordsNotFitException ex) {
                    infoLabel.setText("Das dürfe eigentlich niemals passieren...");
                } catch (SQLException ex) {
                    infoLabel.setText("Username existiert bereits");
                } catch (PasswordToShortException ex) {
                    infoLabel.setText("Passwort ist zu kurz");
                } catch (TooLongUsernameException ex) {
                    infoLabel.setText("Username zu lang");
                } catch (TooLongPasswordException ex) {
                    infoLabel.setText("Passwort ist zu lang");
                } catch (NoUsernameException ex) {
                    infoLabel.setText("Es wurde kein Nutzername eingegeben");
                }

            });

            Button quitButton = new Button("Abbrechen");
            quitButton.setOnAction(e -> {
                AddUserView.this.close();
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
