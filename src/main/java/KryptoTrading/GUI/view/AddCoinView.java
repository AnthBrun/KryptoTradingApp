package KryptoTrading.GUI.view;

import KryptoTrading.Datenhaltung.Coins;
import KryptoTrading.GUI.model.EntryBean;
import KryptoTrading.GUI.model.Globals;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class AddCoinView extends Stage {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final String TITLE = "Coin Hinzufügen";

    private AdminLayout adminLayout;

    public AddCoinView(AdminLayout adminLayout) {
        this.adminLayout = adminLayout;
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(Main.mainStage);
        this.setScene(new Scene(new AddCoinView.AddCoinLayout(), WIDTH, HEIGHT));
        this.setTitle(TITLE);
    }


    public class AddCoinLayout extends BorderPane {

        public AddCoinLayout() {
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

            Label nameLabel = new Label("Name");
            Label shortNameLabel = new Label("Kürzel");
            Label amountLabel = new Label("Menge im Umlauf");
            Label marktCapLabel = new Label("Marktkapitalisierung");
            Label worthLabel = new Label("Wert");

            TextField nameTextField = new TextField();
            TextField shortNameTextField = new TextField();
            TextField amountTextField = new TextField();
            TextField marktCapTextField = new TextField();
            TextField worthTextField = new TextField();

            gp.add(nameLabel, 0, 0);
            gp.add(shortNameLabel, 0, 1);
            gp.add(amountLabel, 0, 2);
            gp.add(marktCapLabel, 0, 3);
            gp.add(worthLabel, 0, 4);

            gp.add(nameTextField, 1, 0);
            gp.add(shortNameTextField, 1,1);
            gp.add(amountTextField, 1,2);
            gp.add(marktCapTextField, 1, 3);
            gp.add(worthTextField, 1, 4);


            Button readyButton = new Button("OK");
            readyButton.setOnAction(e -> {

                Coins c = new Coins();
                c.add(shortNameTextField.getText(), nameTextField.getText(), Double.parseDouble(worthTextField.getText()),
                        Double.parseDouble(marktCapTextField.getText()), Integer.parseInt(amountTextField.getText()));
                adminLayout.refreshTable();
                AddCoinView.this.close();
            });

            Button quitButton = new Button("Abbrechen");
            quitButton.setOnAction(e -> {
                AddCoinView.this.close();
            });


            centerBox.getChildren().addAll(gp);
            buttonBox.getChildren().addAll(readyButton, quitButton);


            this.setCenter(centerBox);
            this.setBottom(buttonBox);

        }

    }




}
