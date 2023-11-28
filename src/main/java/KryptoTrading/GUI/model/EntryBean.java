package KryptoTrading.GUI.model;

import java.io.Serializable;

import javafx.beans.property.StringProperty;

public class EntryBean implements Serializable {
	private String walletAdress;
	private String walletId;
	
	public EntryBean(){}
	
	
	
	public EntryBean(String walletAdress, String walletId) {
		this.walletAdress = walletAdress;
		this.walletId = walletId;
	}
	
	
	
	
	public void setWalletAdress(String walletAdress) {
		this.walletAdress = walletAdress;
	}
	
	public String getWalletAdress() {
		return walletAdress;
	}
	
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	
	public String getWalletId() {
		return walletId;
	}
	
}
