package KryptoTrading.GUI.view;

import KryptoTrading.Datenhaltung.Coins;
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

public class CoinSettingsView extends Stage {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final String TITLE = "Coin bearbeiten";

    private AdminLayout adminLayout;

    public CoinSettingsView(AdminLayout adminLayout) {
        this.adminLayout = adminLayout;
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(Main.mainStage);
        this.setScene(new Scene(new CoinSettingsView.CoinSettingsLayout(), WIDTH, HEIGHT));
        this.setTitle(TITLE);
    }


    public class CoinSettingsLayout extends BorderPane {

        public CoinSettingsLayout() {
            Coins c = (Coins)adminLayout.selected;
            VBox centerBox = new VBox();
            centerBox.setAlignment(Pos.CENTER);
            centerBox.setSpacing(Globals.DEFAULT_SPACING);
            centerBox.setPadding(new Insets(5,5,5,5));


            HBox buttonBox = new HBox();
            buttonBox.setSpacing(Globals.DEFAULT_SPACING);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(5,5,5,5));


            GridPane gp = new GridPane();
            gp.setAlignment(Pos.CENTER);
            gp.setHgap(Globals.DEFAULT_SPACING);
            gp.setVgap(Globals.DEFAULT_SPACING);

            Label nameLabel = new Label("Name");
            Label amountLabel = new Label("Menge im Umlauf");
            Label marktCapLabel = new Label("Marktkapitalisierung");
            Label worthLabel = new Label("Wert");

            TextField nameTextField = new TextField();
            nameTextField.setText(c.getName());
            TextField amountTextField = new TextField();
            amountTextField.setText(""+c.getTotal_amount());
            TextField marktCapTextField = new TextField();
            marktCapTextField.setText(""+c.getMarket_capitalization());
            TextField worthTextField = new TextField();
            worthTextField.setText(""+c.getValue());

            gp.add(nameLabel, 0, 0);
            gp.add(amountLabel, 0, 1);
            gp.add(marktCapLabel, 0, 2);
            gp.add(worthLabel, 0, 3);

            gp.add(nameTextField, 1, 0);
            gp.add(amountTextField, 1,1);
            gp.add(marktCapTextField, 1, 2);
            gp.add(worthTextField, 1, 3);



            Button readyButton = new Button("OK");
            readyButton.setOnAction(e -> {
                c.update(nameTextField.getText(), Double.parseDouble(worthTextField.getText()),
                        Double.parseDouble(marktCapTextField.getText()), Long.parseLong(amountTextField.getText()));

                adminLayout.refreshTable();
                CoinSettingsView.this.close();
            });

            Button quitButton = new Button("Abbrechen");
            quitButton.setOnAction(e -> {
                CoinSettingsView.this.close();
            });


            centerBox.getChildren().addAll(gp);
            buttonBox.getChildren().addAll(readyButton, quitButton);


            this.setCenter(centerBox);
            this.setBottom(buttonBox);


        }

    }


}
