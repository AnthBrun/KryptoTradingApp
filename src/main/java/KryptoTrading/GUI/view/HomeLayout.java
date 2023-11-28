package KryptoTrading.GUI.view;

import java.util.*;

import KryptoTrading.Datenhaltung.User;
import KryptoTrading.Datenhaltung.Wallet;
import KryptoTrading.GUI.model.Globals;
import KryptoTrading.GUI.model.Util;
import KryptoTrading.GUI.model.exceptions.NoEventHandlerException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeLayout extends BorderPane {
	MenuBar bar;
	HBox bottomBox;
	ScrollPane centerScrollPane;
	HomeActionEventHandler homeActionEventHandler;
	HomeMouseEventHandler homeMouseEventHandler;
	
	MenuItem languageItem;
	MenuItem appearanceItem;
	MenuItem preferencesItem;
	MenuItem logoutItem;
	MenuItem versionItem;
	MenuItem showItem;
	
	VBox centerBox;
	
	Button addButton;
	
	
	String username;
	char[] password;
	
	private List<Wallet> wallets;
	private Map<Wallet, Button> walletButtonMap;
	
	
	Wallet focusedWallet;
	
	
	public HomeLayout(String username, char[] password) {
		walletButtonMap = new HashMap<Wallet, Button>();
		Main.mainStage.setResizable(false);
		homeMouseEventHandler = new HomeMouseEventHandler();
		homeActionEventHandler = new HomeActionEventHandler();
		
		this.username = username;
		this.password = password;
		try {
			initEntryButtonMap();
		} catch(Exception e) {

		}

		initMenuBar();
		initBottomBox();

		initCenterScrollPane();
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, new HomeMouseEventHandler());
		bar.addEventHandler(MouseEvent.MOUSE_PRESSED, new HomeMouseEventHandler());
		bottomBox.addEventHandler(MouseEvent.MOUSE_PRESSED, new HomeMouseEventHandler());
		centerBox.addEventHandler(MouseEvent.MOUSE_PRESSED, new HomeMouseEventHandler());
		this.setTop(bar);
		this.setBottom(bottomBox);
		this.setCenter(centerScrollPane);
		
		
	}
	
	
	
	
	
	
	
	
	private void initEntryButtonMap() throws NoEventHandlerException {
		/*
		if (homeActionEventHandler == null) throw new NoEventHandlerException();
		entryButtonMap = new HashMap<EntryBean, Button>();
		if (entrys == null) return;
		for (int i = 0; i < entrys.size(); i++) {
			Button b = new Button(entrys.get(i).getWalletId());
			b.setMaxWidth(900);
			b.setOnAction(homeActionEventHandler);
			Tooltip tooltip = new Tooltip("Passwort in Zwischenablage kopieren");
			b.setTooltip(tooltip);
			entryButtonMap.put(entrys.get(i), b);
			
		}
		 */

		wallets = Wallet.getWalletsOfUser(User.getUser(username).getAddress());
		for (Wallet w : wallets) {
			Button b = new Button(w.getIdentifier_name());
			b.setMaxWidth(900);
			b.setOnAction(homeActionEventHandler);
			Tooltip tooltip = new Tooltip("Inhalt anzeigen");
			b.setTooltip(tooltip);
			walletButtonMap.put(w, b);
		}
	}
	
	
	
	
	
	

	
	
	
	
	public void addEntry(Wallet wallet) {
		wallets.add(wallet);
		Button b = new Button(wallet.getIdentifier_name());
		b.setMaxWidth(900);
		b.setOnAction(homeActionEventHandler);
		walletButtonMap.put(wallet, b);
		centerBox.getChildren().add(b);

	}
	
	
	public void refreshEntry(Wallet wallet) {
		walletButtonMap.get(wallet).setText(wallet.getIdentifier_name());

	}
	
	
	
	public void deleteEntry(Wallet wallet) {
		wallets.remove(wallet);
		centerBox.getChildren().remove(walletButtonMap.get(wallet));
		walletButtonMap.remove(wallet);
		

	}
	
	
	
	
	
	private void initCenterScrollPane() {
		
		centerBox = new VBox();
		centerBox.setAlignment(Pos.TOP_CENTER);
		initEntryButtons();
		centerScrollPane = new ScrollPane(centerBox);
		centerScrollPane.setFitToWidth(true);
		centerScrollPane.setFitToHeight(true);
		
	}


	public void initEntryButtons() {
		for (Wallet w : wallets) {
			centerBox.getChildren().add(walletButtonMap.get(w));
		}
	}
	
	
	
	
	private void initBottomBox() {
		bottomBox = new HBox();
		
		addButton = new Button("Hinzufügen");
		addButton.setPrefWidth(100);
		addButton.setOnAction(homeActionEventHandler);
		//bottomBox.getChildren().addAll(addButton, settingsButton);
		bottomBox.getChildren().addAll(addButton);
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.setSpacing(Globals.DEFAULT_SPACING);
		this.setBottom(bottomBox);
	}
	
	
	
	
	private void initMenuBar() {
		bar = new MenuBar();
		Menu optionsMenu = new Menu("Optionen");
		Menu accountMenu = new Menu("Account");
		Menu aboutMenu = new Menu(Globals.APP_NAME);
		languageItem = new MenuItem("Sprache");
		appearanceItem = new MenuItem("Darstellung");
		appearanceItem.setOnAction(homeActionEventHandler);
		preferencesItem = new MenuItem("Einstellungen");
		logoutItem = new MenuItem("Logout");
		logoutItem.setOnAction(homeActionEventHandler);
		versionItem = new MenuItem("Version");
		versionItem.setOnAction(homeActionEventHandler);

		optionsMenu.getItems().addAll(languageItem, appearanceItem, preferencesItem);
		accountMenu.getItems().addAll(logoutItem);
		aboutMenu.getItems().addAll(versionItem);

		bar.getMenus().addAll(optionsMenu, aboutMenu, accountMenu);
		
	}
	
	
	
	private Wallet getWalletByButton(Button value) {
		Set<Wallet> keys = walletButtonMap.keySet();
		for (Wallet w : keys) {
			if (walletButtonMap.get(w).equals(value)) {
				return w;
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	private class HomeActionEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (event.getSource() == logoutItem) {
				Util.logout();
				return;
			}
			if (event.getSource() == versionItem) {
				Stage versionView = new VersionView();
				versionView.show();
				return;
			}
			if (event.getSource() == addButton) {
				Stage addEntryView = new AddEntryView(username);
				addEntryView.show();
				return;
			}
			
			
			if (event.getSource() == appearanceItem) {
				Stage stage = new AppearanceView();
				stage.show();
			}
			
			
			
			if (walletButtonMap.containsValue(event.getSource())) {
				focusedWallet = getWalletByButton((Button)event.getTarget());
				
				Stage walletView = new WalletView(focusedWallet);
				walletView.show();
				
				return;
			}
			
		}
	}
	
	
	
	
	
	private class HomeMouseEventHandler implements EventHandler<MouseEvent> {
		
		@Override
		public void handle(MouseEvent event) {
			if (walletButtonMap.containsValue(event.getTarget())) {
				focusedWallet = getWalletByButton((Button)event.getTarget());
			} 
		}
	}
	
	
	
	
	
	
	
	
	
	
}
