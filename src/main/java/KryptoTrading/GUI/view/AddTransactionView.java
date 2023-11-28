package KryptoTrading.GUI.view;

import KryptoTrading.Datenhaltung.Transaction;
import KryptoTrading.Datenhaltung.Wallet;
import KryptoTrading.Fachlogik.Transferring;
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

public class AddTransactionView extends Stage {
    AdminLayout adminLayout;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final String TITLE = "Transaktion Hinzufügen";

    public AddTransactionView(AdminLayout adminLayout) {
        this.adminLayout = adminLayout;
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(Main.mainStage);
        this.setScene(new Scene(new AddTransactionView.AddTransactionLayout(), WIDTH, HEIGHT));
        this.setTitle(TITLE);
    }



    public class AddTransactionLayout extends BorderPane {

        public AddTransactionLayout() {
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


            Label sourceLabel = new Label("Quell-Wallet-ID");
            Label targetLabel = new Label("Ziel-Wallet-ID");
            Label shortNameLabel = new Label("Kürzel");

            Label amountLabel = new Label("Menge");


            TextField sourceTextField = new TextField();
            TextField targetTextField = new TextField();
            TextField shortNameTextField = new TextField();

            TextField amountTextField = new TextField();


            gp.add(sourceLabel, 0, 0);
            gp.add(targetLabel, 0, 1);
            gp.add(shortNameLabel, 0, 2);
            gp.add(amountLabel, 0, 3);


            gp.add(sourceTextField, 1, 0);
            gp.add(targetTextField, 1, 1);
            gp.add(shortNameTextField, 1, 2);
            gp.add(amountTextField, 1, 3);


            Button readyButton = new Button("OK");
            readyButton.setOnAction(e -> {

                Transaction t = new Transaction();
                Transferring.transfer_coins(shortNameTextField.getText(), Double.parseDouble(amountTextField.getText()), Wallet.getWallet(sourceTextField.getText()), Wallet.getWallet(targetTextField.getText()));
                adminLayout.refreshTable();
                AddTransactionView.this.close();
            });

            Button quitButton = new Button("Abbrechen");
            quitButton.setOnAction(e -> {
                AddTransactionView.this.close();
            });


            centerBox.getChildren().addAll(gp);
            buttonBox.getChildren().addAll(readyButton, quitButton);


            this.setCenter(centerBox);
            this.setBottom(buttonBox);
        }

    }
}
