package KryptoTrading.GUI.view;

import KryptoTrading.Fachlogik.UserFunctions;
import KryptoTrading.GUI.model.Globals;
import KryptoTrading.GUI.model.exceptions.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class RegisterLayout extends BorderPane {
	
	
	public RegisterLayout() {
		VBox centerBox = new VBox();
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setSpacing(Globals.DEFAULT_SPACING);
		centerBox.setMaxWidth(150);
		centerBox.setPadding(Globals.DEFAULT_CENTER_INSETS);
		
		Label usernameLabel = new Label("Username");
		usernameLabel.setFont(Globals.DEFAULT_FONT);
		Label passwordLabel = new Label("Passwort");
		passwordLabel.setFont(Globals.DEFAULT_FONT);
		Label passwordRepeatLabel = new Label("Passwort wiederholen");
		passwordRepeatLabel.setFont(Globals.DEFAULT_FONT);
		
		Label infoLabel = new Label();
		
		Label registerLabel = new Label("Registrieren");
		registerLabel.setFont(Globals.DEFAULT_HEADER_FONT);
		
		TextField usernameTextField = new TextField();
		PasswordField passwordField = new PasswordField();
		PasswordField passwordRepeatField = new PasswordField();
		
		Button registerButton = new Button("Registrieren");
		Button backButton = new Button("zurück");
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(Globals.DEFAULT_SPACING);
		
		BorderPane.setAlignment(registerLabel, Pos.CENTER);
		BorderPane.setMargin(registerLabel, new Insets(Globals.DEFAULT_SPACING, 0, Globals.DEFAULT_SPACING, 0));
		BorderPane.setAlignment(infoLabel, Pos.CENTER);
		BorderPane.setMargin(infoLabel, new Insets(Globals.DEFAULT_SPACING, 0, Globals.DEFAULT_SPACING, 0));
		
		
		buttonBox.getChildren().addAll(registerButton, backButton);
		centerBox.getChildren().addAll(usernameLabel, usernameTextField, passwordLabel, passwordField, passwordRepeatLabel, passwordRepeatField, buttonBox);
		
		this.setTop(registerLabel);
		this.setCenter(centerBox);
		this.setBottom(infoLabel);
		
		
		
		//EVENT_HANDLING
		backButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.changeScene(new LoginLayout());
			}
		});
		
		
		registerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				try {
					if (UserFunctions.register(usernameTextField.getText(), passwordField.getCharacters().toString(), passwordRepeatField.getCharacters().toString())){
						infoLabel.setText("Erfolgreich Registriert");
					}

					else {
						infoLabel.setText("Ein Fehler ist aufgetreten");
					}

				} catch (PasswordsNotFitException e) {
					infoLabel.setText("Passwörter stimmen nicht überein");
				} catch (SQLException e) {
					infoLabel.setText("Username existiert bereits");
				} catch (PasswordToShortException e) {
                    infoLabel.setText("Das Passwort muss mindestens " + Globals.MIN_PASSWORD_LENGTH + " Zeichen enthalten");
                } catch (TooLongPasswordException e) {
					infoLabel.setText("Passwort darf höchstens " + Globals.MAX_PASSWORD_LENGTH + " Zeichen enthalten");
                } catch (TooLongUsernameException e) {
					infoLabel.setText("Username darf höchstens " + Globals.MAX_USERNAME_LENGTH + " Zeichen enthalten");
                } catch (NoUsernameException e) {
					infoLabel.setText("Bitte einen Username eingeben");
                }
				
			}
		});
		
		
	}

}
