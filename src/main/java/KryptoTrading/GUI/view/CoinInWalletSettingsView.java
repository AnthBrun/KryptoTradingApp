package KryptoTrading.GUI.view;

import KryptoTrading.Datenhaltung.Coin_in_Wallet;
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

public class CoinInWalletSettingsView extends Stage {
    AdminLayout adminLayout;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final String TITLE = "Coin_In_Wallet bearbeiten";

    public CoinInWalletSettingsView(AdminLayout adminLayout) {
        this.adminLayout = adminLayout;
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(Main.mainStage);
        this.setScene(new Scene(new CoinInWalletSettingsView.CoinInWalletSettingsLayout(), WIDTH, HEIGHT));
        this.setTitle(TITLE);
    }


    public class CoinInWalletSettingsLayout extends BorderPane {

        public CoinInWalletSettingsLayout() {
            Coin_in_Wallet c = (Coin_in_Wallet) adminLayout.selected;
            VBox centerBox = new VBox();
            centerBox.setAlignment(Pos.CENTER);

            HBox buttonBox = new HBox();
            buttonBox.setSpacing(Globals.DEFAULT_SPACING);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(5,5,5,5));

            Label shortNameLabel = new Label("Kürzel");
            Label idLabel = new Label("Wallet-ID");
            Label amountLabel = new Label("Menge");

            TextField shortNameTextField = new TextField();
            shortNameTextField.setText(c.getShortname());
            TextField idTextField = new TextField();
            idTextField.setText(c.getWallet_id());
            TextField amountTextField = new TextField();
            amountTextField.setText(Double.toString(c.getAmount()));

            GridPane gp = new GridPane();
            gp.setAlignment(Pos.CENTER);
            gp.setHgap(Globals.DEFAULT_SPACING);
            gp.setVgap(Globals.DEFAULT_SPACING);


            gp.add(shortNameLabel, 0, 0);
            gp.add(idLabel, 0, 1);
            gp.add(amountLabel, 0, 2);
            gp.add(shortNameTextField, 1, 0);
            gp.add(idTextField, 1, 1);
            gp.add(amountTextField, 1, 2);




            Label infoLabel = new Label();
            infoLabel.setAlignment(Pos.BOTTOM_CENTER);

            Button readyButton = new Button("OK");
            readyButton.setOnAction(e -> {
                c.update(shortNameTextField.getText(), idTextField.getText(), Double.parseDouble(amountTextField.getText()));
                adminLayout.refreshTable();
                CoinInWalletSettingsView.this.close();

            });

            Button quitButton = new Button("Abbrechen");
            quitButton.setOnAction(e -> {
                CoinInWalletSettingsView.this.close();
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
