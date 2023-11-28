package KryptoTrading.GUI.view;


import java.io.IOException;

import KryptoTrading.GUI.model.ConfigBean;
import KryptoTrading.GUI.model.Globals;
import KryptoTrading.GUI.model.Util;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static Stage mainStage;
	public static ConfigBean config;
	public static Application instance;



	public static void main(String[] args) {
		System.out.println("start programm");
		try {
			config = Util.loadConfig();
		} catch (ClassNotFoundException | IOException e) {
			config = new ConfigBean();
			System.out.println("created config!");
		}
		launch();
	}

	



	@Override
	public void start(Stage stage) throws Exception {
		instance = this;
		mainStage = stage;
		stage.setResizable(false);
		//changeScene(new LoginLayout());

		Scene s = new Scene(new LoginLayout(), Globals.WINDOW_WIDTH, Globals.WINDOW_HEIGHT);
		if(Main.config.isDarkMode()) s.setFill(Globals.DARK_MODE_BACKGROUND_COLOR);
		mainStage.setScene(s);

		mainStage.setTitle(Globals.WINDOW_TITLE);
		mainStage.show();
	}









	
	
	
	
	public static void changeScene(Parent Layout) {
		Scene s = new Scene(Layout, Globals.WINDOW_WIDTH, Globals.WINDOW_HEIGHT);
		if(Main.config.isDarkMode()) s.setFill(Globals.DARK_MODE_BACKGROUND_COLOR);
		mainStage.setScene(s);
	}
	
	
	
	
	
	
}
