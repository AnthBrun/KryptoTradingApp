package KryptoTrading.Fachlogik;

import KryptoTrading.Datenhaltung.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WalletCoinMerge {
    private final static Connection connection = DatabaseConnector.getConnection();
    private String name;
    private String identifier_name;
    private String shortname;
    private double menge;
    private final User user;

    public WalletCoinMerge(User user){
        this.user = user;
        identifier_name = "";
        name = "";
        shortname = "";
        menge = 0;
    }

    public void setMenge(double menge) {
        this.menge = menge;
    }

    public void setIdentifier_name(String identifier_name) {
        this.identifier_name = identifier_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public static List<WalletCoinMerge> getTable(User user){
        List<WalletCoinMerge> table = new ArrayList<>();
        String address = user.getData("adresse");

        String sqlQuery = "SELECT * FROM WALLET W JOIN COIN_IN_WALLET CIW ON W.wallet_id = CIW.wallet_id JOIN COINS C ON CIW.KUERZEL = C.KUERZEL " +
                "WHERE adresse = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, address);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String name = resultSet.getString("name");
                    String shortname = resultSet.getString("kuerzel");
                    String wallet_id = resultSet.getString("wallet_id");
                    String identifier_name = resultSet.getString("identifier_name");
                    Double menge = resultSet.getDouble("menge");

                    WalletCoinMerge wcm = new WalletCoinMerge(user);
                    table.add(wcm);
                    System.out.printf("%s contains %f of %s\n", identifier_name, menge, name);
                }
            }
        } catch (SQLException e){
            System.out.println("not able to find wallets");
        }
        return table;
    }

    @Override
    public String toString() {
        return this.identifier_name + ": " + shortname + ": " + menge;
    }
}
