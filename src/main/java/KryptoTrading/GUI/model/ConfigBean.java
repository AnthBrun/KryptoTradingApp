package KryptoTrading.GUI.model;

import java.io.Serializable;
import java.nio.CharBuffer;

public class ConfigBean implements Serializable {
	private boolean rememberMe;
	private String lastUsername;
	private char[] lastPassword;
	private boolean darkMode;
	
	
	public ConfigBean() {
		rememberMe = false;
		lastUsername = "";
		lastPassword = new char[0];
	}
	
	
	
	
	public String getLastUsername() {
		return lastUsername;
	}
	
	
	public void setLastUsername(String newUsername) {
		lastUsername = newUsername;
	}
	
	
	public char[] getLastPassword() {
		return lastPassword;
	}
	
	
	public void setLastPassword(char[] newPassword) {
		lastPassword = newPassword;
	}
	
	
	public boolean getRememberMe() {
		return rememberMe;
	}
	
	
	public void setRememberMe(boolean b) {
		rememberMe = b;
	}

	public boolean isDarkMode() {
		return darkMode;
	}

	public void setDarkMode(boolean darkMode) {
		this.darkMode = darkMode;
	}
}
