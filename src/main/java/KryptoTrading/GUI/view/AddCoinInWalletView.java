package KryptoTrading.GUI.view;

import KryptoTrading.Datenhaltung.Coins;
import KryptoTrading.Datenhaltung.Wallet;
import KryptoTrading.Fachlogik.CoinFunctions;
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

public class AddCoinInWalletView extends Stage {

    AdminLayout adminLayout;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final String TITLE = "Coin_in_Wallet Hinzufügen";

    public AddCoinInWalletView(AdminLayout adminLayout) {
        this.adminLayout = adminLayout;
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(Main.mainStage);
        this.setScene(new Scene(new AddCoinInWalletView.AddCoinInWalletLayout(), WIDTH, HEIGHT));
        this.setTitle(TITLE);
    }


    public class AddCoinInWalletLayout extends BorderPane {

        public AddCoinInWalletLayout() {
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


            Label kuerzelLabel = new Label("Kürzel");
            Label walletIdLabel = new Label("Wallet-ID");
            Label amountLabel = new Label("Menge");

            TextField kuerzelTextField = new TextField();
            TextField walletIdTextField = new TextField();
            TextField amountTextField = new TextField();

            gp.add(kuerzelLabel, 0, 0);
            gp.add(walletIdLabel, 0, 1);
            gp.add(amountLabel, 0, 2);
            gp.add(kuerzelTextField, 1, 0);
            gp.add(walletIdTextField, 1, 1);
            gp.add(amountTextField, 1, 2);



            Button readyButton = new Button("OK");
            readyButton.setOnAction(e -> {

                CoinFunctions.add_coins_to_wallet(kuerzelTextField.getText(), Double.parseDouble(amountTextField.getText()), Wallet.getWallet(walletIdTextField.getText()));
                adminLayout.refreshTable();
                AddCoinInWalletView.this.close();
            });

            Button quitButton = new Button("Abbrechen");
            quitButton.setOnAction(e -> {
                AddCoinInWalletView.this.close();
            });


            centerBox.getChildren().addAll(gp);
            buttonBox.getChildren().addAll(readyButton, quitButton);


            this.setCenter(centerBox);
            this.setBottom(buttonBox);
        }
    }

}
