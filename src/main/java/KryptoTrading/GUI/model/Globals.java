package KryptoTrading.GUI.model;


import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Globals {


	public static final double WINDOW_WIDTH = 350;
	public static final double WINDOW_HEIGHT = 400;

	public static final int INITIAL_COLUMN_WIDTH = 150;
	public static final double WINDOW_WIDTH_ADMIN = 1000;
	public static final double WINDOW_HEIGHT_ADMIN = 600;
	public static final String WINDOW_TITLE = "Kryptonia";
	public static final String APP_NAME = "Kryptonia";
	public static final String VERSION = "0.9";
	public static final String ADMIN_NAME = "admin";
	
	public static final double DEFAULT_SPACING = 5;
	public static final Font DEFAULT_FONT = Font.font("Arial", FontWeight.SEMI_BOLD, 13);
	public static final Font DEFAULT_HEADER_FONT = Font.font("Arial", FontWeight.BOLD, 14);
	public static final Insets DEFAULT_CENTER_INSETS = new Insets(-70, 0, 0, 0);

	public static final Color DARK_MODE_BACKGROUND_COLOR = new Color(60.0/255.0,60.0/255.0,60.0/255.0,1);
	public static final Color DARK_MODE_BUTTON_COLOR = Color.BLACK;
	public static final Color DARK_MODE_FONT_COLOR = Color.WHITE;
	public static final Color DARK_MODE_TextFieldColor = new Color(15.0/255.0,15.0/255.0,15.0/255.0,1);
	
	
	public static final int MIN_PASSWORD_LENGTH = 6;
	public static final int MAX_PASSWORD_LENGTH = 80;
	public static final int MAX_USERNAME_LENGTH = 80;
	public static final int MAX_BUFFER_LENGTH = 4096;
	
	public static final String DATA_PATH = "data";
	public static final String CONFIG_PATH = "data/config.txt";
	public static final String USER_DIRECTORY_SUFFIX = "Data";			// davor der username
	public static final String LOGIN_DATA_SUFFIX = "LoginData.txt";		// davor der username
	public static final String ENTRY_DATA_SUFFIG = "EntryData.txt";					// davor der username
	public static final char SEPARATOR = '\n';
	
	
	

}
