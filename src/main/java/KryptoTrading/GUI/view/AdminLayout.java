package KryptoTrading.GUI.view;

import java.io.IOException;
import java.util.*;

import KryptoTrading.Datenhaltung.*;
import KryptoTrading.GUI.model.EntryBean;
import KryptoTrading.GUI.model.Globals;
import KryptoTrading.GUI.model.Util;
import KryptoTrading.GUI.model.exceptions.NoEventHandlerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AdminLayout extends BorderPane {
	MenuBar bar;
	HBox bottomBox;
	ScrollPane centerScrollPane;
	AdminActionEventHandler adminActionEventHandler;
	
	MenuItem languageItem;
	MenuItem appearanceItem;
	MenuItem preferencesItem;
	MenuItem logoutItem;
	MenuItem versionItem;
	
	MenuItem coinItem;
	MenuItem walletItem;
	MenuItem coinInWalletItem;
	MenuItem userItem;
	MenuItem transactionItem;
	
	
	VBox centerBox;
	
	Button addButton;
	Button changeButton;
	Button deleteButton;
	
	Editable selected;
	String username;
	char[] password;
	
	private ArrayList<EntryBean> entrys;
	private Map<EntryBean, Button> entryButtonMap;
	
	private ObservableList<Coins> coins;
	private ObservableList<User> user;
	private ObservableList<Transaction> transaction;
	private ObservableList<Coin_in_Wallet> coinInWallet;
	private ObservableList<Wallet> wallet;
	
	TableView<Coins> coinTable;
	TableView<User> userTable;
	TableView<Transaction> transactionTable;
	TableView<Coin_in_Wallet> coinInWalletTable;
	TableView<Wallet> walletTable;
	
	EntryBean focusedEntry;

	TableInView tableInView;


	public enum TableInView {
		COINS, USER, WALLET, TRANSACTION, COIN_IN_WALLET
	}




	
	public AdminLayout(String username, char[] password) {
		if(Main.config.isDarkMode())this.setBackground(Background.EMPTY);

		Main.mainStage.setResizable(true);
		Main.mainStage.setWidth(Globals.WINDOW_WIDTH_ADMIN);
		Main.mainStage.setHeight(Globals.WINDOW_HEIGHT_ADMIN);

		adminActionEventHandler = new AdminActionEventHandler();
		
		this.username = username;
		this.password = password;
		try {
			entrys = Util.loadEntrys(username);
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("keine entrys");
			entrys = new ArrayList<EntryBean>();
		}
		try {
			initEntryButtonMap();
		} catch (NoEventHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initMenuBar();
		initBottomBox();

		this.setTop(bar);
		this.setBottom(bottomBox);
		this.setCenter(centerScrollPane);

		coins = FXCollections.observableArrayList();
		setCoinTable();
		
		
	}
	

	

	
	
	private void initEntryButtonMap() throws NoEventHandlerException {
		if (adminActionEventHandler == null) throw new NoEventHandlerException();
		entryButtonMap = new HashMap<EntryBean, Button>();
		if (entrys == null) return;
		for (int i = 0; i < entrys.size(); i++) {
			Button b = new Button(entrys.get(i).getWalletId());
			b.setMaxWidth(900);
			b.setOnAction(adminActionEventHandler);
			Tooltip tooltip = new Tooltip("Passwort in Zwischenablage kopieren");
			b.setTooltip(tooltip);
			entryButtonMap.put(entrys.get(i), b);
			
		}
	}
	
	
	


	
	
	public void refreshTable() {
		switch (tableInView) {
			case COINS :
				setCoinTable();
				break;
			case WALLET :
				setWalletTable();
				break;
			case USER :
				setUserTable();
				break;
			case TRANSACTION:
				setTransactionTable();
				break;
			case COIN_IN_WALLET:
				setCointInWalletTable();
		}
	}
	



	public void addEntry(EntryBean entry) {
		entrys.add(entry);
		Button b = new Button(entry.getWalletId());
		b.setMaxWidth(900);
		b.setOnAction(adminActionEventHandler);
		
		entryButtonMap.put(entry, b);
		centerBox.getChildren().add(b);
		try {
			Util.saveEntrys(username, entrys);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	
	
	private void initBottomBox() {
		bottomBox = new HBox();
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.setSpacing(Globals.DEFAULT_SPACING);

		addButton = new Button("Hinzufügen");
		addButton.setOnAction(adminActionEventHandler);

		changeButton = new Button("Bearbeiten");
		changeButton.setOnAction(adminActionEventHandler);

		deleteButton = new Button("Entfernen");
		deleteButton.setOnAction(adminActionEventHandler);

		bottomBox.getChildren().addAll(addButton, changeButton, deleteButton);
		this.setBottom(bottomBox);
	}
	





	private void setTransactionTable() {
		transaction = FXCollections.observableArrayList();
		List<Transaction> transactionList = Transaction.getTransactions();
		for (Transaction t: transactionList) {
			transaction.add(t);
		}

		TableColumn<Transaction, String> idColumn = new TableColumn<>("Transaktions-ID");
		idColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		idColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transaction_id"));

		TableColumn<Transaction, String> sourceColumn = new TableColumn<>("Quelle(Wallet-ID)");
		sourceColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		sourceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("sourceString"));

		TableColumn<Transaction, String> targetColumn = new TableColumn<>("Ziel(Wallet-ID)");
		targetColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		targetColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("targetString"));

		TableColumn<Transaction, String> kuerzelColumn = new TableColumn<>("Kürzel");
		kuerzelColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		kuerzelColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("shortname"));

		TableColumn<Transaction, Integer> nextColumn = new TableColumn<>("Nächste Transaktion(ID)");
		nextColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		nextColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("next"));

		TableColumn<Transaction, Integer> previousColumn = new TableColumn<>("Vorherige Transaktion(ID)");
		previousColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		previousColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("previous"));

		TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Menge");
		amountColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		amountColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));




		transactionTable = new TableView<>();
		transactionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		transactionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selected = newSelection;
			}
		});

		transactionTable.setItems(transaction);
		transactionTable.getColumns().addAll(idColumn, sourceColumn, targetColumn, kuerzelColumn, nextColumn, previousColumn, amountColumn);
		this.setCenter(transactionTable);
		tableInView = TableInView.TRANSACTION;
	}







	
	private void setCoinTable() {
		coins = FXCollections.observableArrayList();
		List<Coins> coinsList = Coins.getCoins();
		for (Coins c: coinsList) {
			coins.add(c);
		}

		System.out.println("coins:" + coins.size());
		System.out.println("coinsList:" + coinsList.size());

		TableColumn<Coins, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		nameColumn.setCellValueFactory(new PropertyValueFactory<Coins, String>("name"));

		TableColumn<Coins, String> shortNameColumn = new TableColumn<>("Kuerzel");
		shortNameColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		shortNameColumn.setCellValueFactory(new PropertyValueFactory<Coins, String>("shortname"));
		
		TableColumn<Coins, Integer> amountColumn = new TableColumn<>("Menge");
		amountColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		amountColumn.setCellValueFactory(new PropertyValueFactory<Coins, Integer>("total_amount"));

		TableColumn<Coins, Double> marktCapColumn = new TableColumn<>("Marktkapitalisierung");
		marktCapColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		marktCapColumn.setCellValueFactory(new PropertyValueFactory<Coins, Double>("market_capitalization"));


		TableColumn<Coins, Double> worthColumn = new TableColumn<>("Wert");
		worthColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		worthColumn.setCellValueFactory(new PropertyValueFactory<Coins, Double>("value"));


		coinTable = new TableView<>();
		if(Main.config.isDarkMode()) coinTable.setBackground(Background.fill(Globals.DARK_MODE_BACKGROUND_COLOR));



		coinTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		coinTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selected = newSelection;
			}
		});

		coinTable.setItems(coins);
		coinTable.getColumns().addAll(nameColumn,shortNameColumn, amountColumn, marktCapColumn, worthColumn);
		this.setCenter(coinTable);
		tableInView = TableInView.COINS;
	}





	private void setUserTable() {
		user = FXCollections.observableArrayList();
		List<User> userList = User.getUser();
		for (User b: userList) {
			user.add(b);
		}
		TableColumn<User, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

		TableColumn<User, String> adresseColumn = new TableColumn<>("Adresse");
		adresseColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		adresseColumn.setCellValueFactory(new PropertyValueFactory<User, String>("address"));

		TableColumn<User, String> passHashColumn = new TableColumn<>("Passwort-Hash");
		passHashColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		passHashColumn.setCellValueFactory(new PropertyValueFactory<User, String>("passw"));


		userTable = new TableView<>();
		userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						selected = newSelection;
					}
				});
		userTable.setItems(user);
		userTable.getColumns().addAll(nameColumn, adresseColumn, passHashColumn);
		this.setCenter(userTable);
		tableInView = TableInView.USER;
	}





	private void setWalletTable() {
		wallet = FXCollections.observableArrayList();
		List<Wallet> walletList = Wallet.getWallets();

		for (Wallet w: walletList) {
			wallet.add(w);
		}
		System.out.println("WALLETID: " + wallet.get(0).getWallet_id());

		TableColumn<Wallet, String> idColumn = new TableColumn<>("Wallet-ID");
		idColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		idColumn.setCellValueFactory(new PropertyValueFactory<Wallet, String>("wallet_id"));

		TableColumn<Wallet, String> adresseColumn = new TableColumn<>("Benutzer-Adresse");
		adresseColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		adresseColumn.setCellValueFactory(new PropertyValueFactory<Wallet, String>("address"));

		TableColumn<Wallet, String> nameColumn = new TableColumn<>("Identifier-Name");
		nameColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		nameColumn.setCellValueFactory(new PropertyValueFactory<Wallet, String>("identifier_name"));


		walletTable = new TableView<>();
		walletTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		walletTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selected = newSelection;
			}
		});
		walletTable.setItems(wallet);
		walletTable.getColumns().addAll(idColumn, nameColumn, adresseColumn);
		this.setCenter(walletTable);
		tableInView = TableInView.WALLET;
	}





	private void setCointInWalletTable() {
		coinInWallet = FXCollections.observableArrayList();
		List<Coin_in_Wallet> ciwList = Coin_in_Wallet.getCoinsInWallet();

		for (Coin_in_Wallet ciw : ciwList) {
			coinInWallet.add(ciw);
		}

		TableColumn<Coin_in_Wallet, String> kuerzelColumn = new TableColumn<>("Kuerzel");
		kuerzelColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		kuerzelColumn.setCellValueFactory(new PropertyValueFactory<Coin_in_Wallet, String>("shortname"));

		TableColumn<Coin_in_Wallet, String> idColumn = new TableColumn<>("Wallet-ID");
		idColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		idColumn.setCellValueFactory(new PropertyValueFactory<Coin_in_Wallet, String>("wallet_id"));

		TableColumn<Coin_in_Wallet, Double> amountColumn = new TableColumn<>("Menge");
		amountColumn.setMinWidth(Globals.INITIAL_COLUMN_WIDTH);
		amountColumn.setCellValueFactory(new PropertyValueFactory<Coin_in_Wallet, Double>("amount"));




		coinInWalletTable = new TableView<>();
		coinInWalletTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		coinInWalletTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selected = newSelection;
			}
		});
		coinInWalletTable.setItems(coinInWallet);
		coinInWalletTable.getColumns().addAll(kuerzelColumn, idColumn, amountColumn);
		this.setCenter(coinInWalletTable);
		tableInView = TableInView.COIN_IN_WALLET;
	}
	



	private void initMenuBar() {
		bar = new MenuBar();
		Menu optionsMenu = new Menu("Optionen");
		Menu accountMenu = new Menu("Account");
		Menu aboutMenu = new Menu(Globals.APP_NAME);
		Menu tablesMenu = new Menu("Tabellen");
		
		languageItem = new MenuItem("Sprache");
		appearanceItem = new MenuItem("Darstellung");
		appearanceItem.setOnAction(adminActionEventHandler);
		preferencesItem = new MenuItem("Einstellungen");
		logoutItem = new MenuItem("Logout");
		logoutItem.setOnAction(adminActionEventHandler);
		versionItem = new MenuItem("Version");
		versionItem.setOnAction(adminActionEventHandler);
		
		coinItem = new MenuItem("Coin");
		coinItem.setOnAction(adminActionEventHandler);
		
		walletItem = new MenuItem("Wallet");
		walletItem.setOnAction(adminActionEventHandler);
		
		coinInWalletItem = new MenuItem("Coin in Wallet");
		coinInWalletItem.setOnAction(adminActionEventHandler);
		
		userItem = new MenuItem("User");
		userItem.setOnAction(adminActionEventHandler);
		
		transactionItem = new MenuItem("Transaktion");
		transactionItem.setOnAction(adminActionEventHandler);
		
		
		optionsMenu.getItems().addAll(languageItem, appearanceItem, preferencesItem);
		accountMenu.getItems().addAll(logoutItem);
		aboutMenu.getItems().addAll(versionItem);
		tablesMenu.getItems().addAll(coinItem, walletItem, coinInWalletItem, userItem, transactionItem);
		
		bar.getMenus().addAll(optionsMenu, aboutMenu, tablesMenu, accountMenu);
		
	}

	




	
	private class AdminActionEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (event.getSource() == logoutItem) {
				Util.logout();

			}else
			if (event.getSource() == versionItem) {
				Stage versionView = new VersionView();
				versionView.show();

			}else

			
			
			if (event.getSource() == appearanceItem) {
				Stage stage = new AppearanceView();
				stage.show();
			}else
			
			
			if(event.getSource() == coinItem) {
				setCoinTable();

			}else

			if(event.getSource() == userItem) {
				setUserTable();
			}else

			if (event.getSource() == walletItem) {
				setWalletTable();
			}else


			if (event.getSource() == transactionItem) {
				setTransactionTable();
			}else


			if (event.getSource() == coinInWalletItem) {
				setCointInWalletTable();
			}else

			if (event.getSource() == addButton) {
				switch(tableInView) {
					case COINS:
						Stage addCoinView = new AddCoinView(AdminLayout.this);
						addCoinView.show();
						break;
					case USER:
						Stage addUserView = new AddUserView(AdminLayout.this);
						addUserView.show();
						break;
					case WALLET:
						Stage addWalletView = new AddWalletView(AdminLayout.this);
						addWalletView.show();
						break;
					case TRANSACTION:
						Stage addTransactionView = new AddTransactionView(AdminLayout.this);
						addTransactionView.show();
						break;
					case COIN_IN_WALLET:
						Stage addCoinInWalletView = new AddCoinInWalletView(AdminLayout.this);
						addCoinInWalletView.show();

				}

			}else

			if(event.getSource() == changeButton) {
				switch(tableInView) {
					case COINS :
						Stage coinSettingsView = new CoinSettingsView(AdminLayout.this);
						coinSettingsView.show();
						break;
					case USER:
						Stage userSettingsView = new UserSettingsView(AdminLayout.this);
						userSettingsView.show();
						break;
					case WALLET:
						Stage walletSettingsView = new WalletSettingsView(AdminLayout.this);
						walletSettingsView.show();
						break;
					case TRANSACTION:
						Stage transactionSettingsView = new TransactionSettingsView(AdminLayout.this);
						transactionSettingsView.show();
						break;
					case COIN_IN_WALLET:
						Stage coinInWalletSettingsView = new CoinInWalletSettingsView(AdminLayout.this);
						coinInWalletSettingsView.show();
				}

			}else

			if (event.getSource() == deleteButton) {
				selected.remove();
				refreshTable();

			}

			
		}
	}
	

	
}
