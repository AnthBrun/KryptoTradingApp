package KryptoTrading.GUI.view;

import java.io.IOException;

import KryptoTrading.Datenhaltung.User;
import KryptoTrading.Fachlogik.UserFunctions;
import KryptoTrading.GUI.model.Globals;
import KryptoTrading.GUI.model.Util;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginLayout extends BorderPane {
	
	public LoginLayout() {
		if(Main.config.isDarkMode())this.setBackground(Background.EMPTY);
		Main.mainStage.setWidth(Globals.WINDOW_WIDTH);
		Main.mainStage.setHeight(Globals.WINDOW_HEIGHT);

		VBox mainBox = new VBox();
		mainBox.setMaxSize(150,200);
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(Globals.DEFAULT_SPACING);
		mainBox.setPadding(Globals.DEFAULT_CENTER_INSETS);

		
		Label usernameLabel = new Label("Username");
		if(Main.config.isDarkMode())usernameLabel.setTextFill(Globals.DARK_MODE_FONT_COLOR);
		usernameLabel.setFont(Globals.DEFAULT_FONT);

		Label passwordLabel = new Label("Passwort");
		if(Main.config.isDarkMode())passwordLabel.setTextFill(Globals.DARK_MODE_FONT_COLOR);
		passwordLabel.setFont(Globals.DEFAULT_FONT);
		
		Label infoLabel = new Label();
		if(Main.config.isDarkMode()) infoLabel.setTextFill(Globals.DARK_MODE_FONT_COLOR);
		BorderPane.setAlignment(infoLabel, Pos.CENTER);
		
		TextField usernameTextField = new TextField();

		usernameTextField.setPrefWidth(30);
		PasswordField passwordField = new PasswordField();
		passwordField.setPrefWidth(30);



		if (Main.config.getRememberMe()) {
			usernameTextField.setText(Main.config.getLastUsername());
			passwordField.setText(new String(Main.config.getLastPassword()));
			
		}
		
		Label loginLabel = new Label("LogIn");
		if(Main.config.isDarkMode())loginLabel.setTextFill(Globals.DARK_MODE_FONT_COLOR);
		loginLabel.setFont(Globals.DEFAULT_HEADER_FONT);
		loginLabel.setPadding(new Insets(Globals.DEFAULT_SPACING,0,0,0));
		
		
		HBox topBox = new HBox();
		topBox.setAlignment(Pos.CENTER);
		
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		
		Button loginButton = new Button("Login");
		Button registerButton = new Button("Registrieren");
		
		
		HBox rememberMeBox = new HBox();
		CheckBox rememberMeCheckBox = new CheckBox("Remember Me");
		if (Main.config.isDarkMode()) rememberMeCheckBox.setTextFill(Globals.DARK_MODE_FONT_COLOR);
		rememberMeCheckBox.setSelected(Main.config.getRememberMe());
		rememberMeBox.setSpacing(Globals.DEFAULT_SPACING);
		rememberMeBox.setAlignment(Pos.CENTER);
		rememberMeBox.getChildren().addAll(rememberMeCheckBox);
		
		
		buttonBox.getChildren().addAll(loginButton, registerButton);
		mainBox.getChildren().addAll(usernameLabel, usernameTextField, passwordLabel, passwordField, buttonBox, rememberMeBox);
		topBox.getChildren().add(loginLabel);
		
		
		//EVENT HANDLING
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				User user = new User();
				if (UserFunctions.login(usernameTextField.getText(), passwordField.getText())){
					if (usernameTextField.getText().equals(Globals.ADMIN_NAME)) Main.changeScene(new AdminLayout(usernameTextField.getText(), passwordField.getText().toCharArray()));
					else Main.changeScene(new HomeLayout(usernameTextField.getText(), passwordField.getText().toCharArray()));

					if (rememberMeCheckBox.isSelected()) {
						Main.config.setLastUsername(usernameTextField.getText());
						Main.config.setLastPassword(passwordField.getCharacters().toString().toCharArray());
						try {
							Util.saveConfig(Main.config);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}

				}
				else {
					infoLabel.setText("Username oder Passwort falsch");
				}
				
				
			}
		});
		
		
		registerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.changeScene(new RegisterLayout());
			}
		});
		
		
		rememberMeCheckBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Main.config.setRememberMe(rememberMeCheckBox.isSelected());
					if (!rememberMeCheckBox.isSelected()) {
						Main.config.setLastPassword(new char[]{});
						Main.config.setLastUsername("");
					}
					
					Util.saveConfig(Main.config);
				} catch (IOException e) {
					infoLabel.setText("etwas ist schiefgelaufen, warten sie auf das n�chste update und bewahren sie ruhe");

				}
			}
		});
		
		
		this.setTop(topBox);
		this.setCenter(mainBox);
		this.setBottom(infoLabel);


	}
	
	
	
	
	
}
