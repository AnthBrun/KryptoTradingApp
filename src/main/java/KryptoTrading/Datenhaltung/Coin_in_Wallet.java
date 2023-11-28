package KryptoTrading.Datenhaltung;

import KryptoTrading.Fachlogik.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Coin_in_Wallet implements Editable{
    private String shortname;
    private String wallet_id;
    private double amount;
    private final static Connection connection = DatabaseConnector.getConnection();
    private final String[] getData_param_list = {"wallet_id", "amount", "shortname"};
    private final Wallet wallet;

    public Coin_in_Wallet(Wallet wallet){
        this.wallet = wallet;
    }

    public Coin_in_Wallet(String shortname, String wallet_id, double amount){
        this.wallet = Wallet.getWallet(wallet_id);
        this.shortname = shortname;
        this.wallet_id = wallet_id;
        this.amount = amount;
    }


    // liste wird von allen datensätzen in der tabelle wird für gui benötigt
    public static List<Coin_in_Wallet> getCoinsInWallet() {
        List<Coin_in_Wallet> ciwList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM COIN_IN_WALLET";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    String shortname = resultSet.getString("shortname");
                    String wallet_id = resultSet.getString("wallet_id");
                    double amount = resultSet.getDouble("amount");

                    Coin_in_Wallet ciw = new Coin_in_Wallet(shortname, wallet_id, amount);
                    ciwList.add(ciw);
                    System.out.println(ciw.getData("shortname") + " added to list");
                }
            }
        } catch (SQLException e){
            System.out.println("failed to collect user");
        }
        return ciwList;
    }

    public static List<String> get_all_Coins_in_Wallet(Wallet wallet){
        ArrayList<String> coins = new ArrayList<>();
        String sqlQuery = "SELECT CIW.shortname, C.name FROM COIN_IN_WALLET CIW " +
                "JOIN COINS C ON CIW.shortname = C. shortname " +
                "WHERE CIW.wallet_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, wallet.getWallet_id());
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet != null && resultSet.next()){
                    coins.add(resultSet.getString("shortname"));
                }
            }
        } catch (SQLException e){
            System.out.println("failed to list all Coin_in_Wallet list elements");
        }
        return coins;
    }

    public static Coin_in_Wallet getCoin_in_wallet(Wallet wallet){
        Coin_in_Wallet coins_in_wallet = new Coin_in_Wallet(wallet);
        String sqlQuery = "SELECT CIW.amount, CIW.shortname, CIW.wallet_id FROM COIN_IN_WALLET CIW " +
                "JOIN WALLET W ON CIW.wallet_id = W.wallet_id " +
                "WHERE CIW.wallet_id =  ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, wallet.getWallet_id());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    resultSet.getString("shortname");
                    resultSet.getDouble("amount");
                }
            }
        } catch (SQLException e){
            System.out.println("failed to connect to the coins_in_wallet");
        }
        return coins_in_wallet;
    }

    public static List<Coin_in_Wallet> getCoin_in_walletForGui(Wallet wallet){
        List<Coin_in_Wallet> coins = new ArrayList<Coin_in_Wallet>();
        String sqlQuery = "SELECT CIW.amount, CIW.shortname, CIW.wallet_id FROM COIN_IN_WALLET CIW " +
                "JOIN WALLET W ON CIW.wallet_id = W.wallet_id " +
                "WHERE CIW.wallet_id =  ?";

        // Eigentliche SQL-Abfrage
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, wallet.getWallet_id());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet != null && resultSet.next()) {
                    coins.add(new Coin_in_Wallet(resultSet.getString("shortname"), wallet.getWallet_id(), resultSet.getDouble("amount")));
                }
            }
        } catch (SQLException e){
            System.out.println("failed to connect to the coins_in_wallet");
        }
        return coins;
    }

    public String getData(String param) {
        String data = null;
        if (DatabaseConnector.is_valid_param(param, getData_param_list)) {
            // Allgemeiner SQL-Befehl als String
            String sqlQuery = String.format("SELECT %s FROM COIN_IN_WALLET WHERE wallet_id = ?", param);

            // Eigentliche SQL-Abfrage
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, this.wallet.getWallet_id());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet != null && resultSet.next()) {
                        data = resultSet.getString(param);
                    }
                }
            } catch (SQLException e) {
                System.out.println("failed to get data from coins_in_wallet");
            }
            return data;
        } else return null;
    }

    public static double getCoin_amount(Wallet wallet, String shortname){
        double amount = 0;
        String sqlQuery = "SELECT shortname, amount FROM COIN_IN_WALLET where wallet_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, wallet.getWallet_id());
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet != null && resultSet.next()){
                    double quantity = resultSet.getDouble("amount");
                    String this_shortname = resultSet.getString("shortname");
                    if (this_shortname.equals(shortname)) {
                        System.out.printf("%s: %f\n", this_shortname, quantity);
                        return quantity;
                    }
                }
                System.out.printf("no %s in wallet\n", shortname);
            }
        } catch (SQLException e){
            System.out.println("failed to display amount");
        }
        return amount;
    }

    public static boolean check_amount(Wallet wallet, String shortname, double amount) {
        String sqlQuery = "SELECT amount FROM COIN_IN_WALLET WHERE wallet_id = ? AND shortname = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, wallet.getWallet_id());
            preparedStatement.setString(2, shortname);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()){
                    double quantity = resultSet.getDouble("amount");
                    if (quantity >= quantity) return true;
                }
            }
        } catch (SQLException e){
            System.out.println("failed to check amount of the wallet");
        }
        return false;
    }

    public static boolean check_coin_in_wallet(Wallet wallet, String shortname){
        String sqlQuery = "SELECT shortname FROM COIN_IN_WALLET WHERE wallet_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, wallet.getWallet_id());
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet != null && resultSet.next()){
                    String this_shortname = resultSet.getString("shortname");
                    if (this_shortname.equals(shortname)) return true;
                }
            }
        } catch (SQLException e){
            System.out.println("could not check if coin is in this wallet");
        }
        return false;
    }

    public static void insert_new_coin(Wallet wallet, String shortname, double amount){
        String sqlQuery = "INSERT INTO COIN_IN_WALLET VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, shortname);
            preparedStatement.setString(2, wallet.getWallet_id());
            preparedStatement.setDouble(3, amount);
            int inserted_rows = preparedStatement.executeUpdate();

            if(inserted_rows > 0){
                System.out.printf("%f of %s to %s added\n", amount, shortname, wallet.getWallet_id());
            } else System.out.printf("failed to add new coin to wallet %s\n", wallet.getWallet_id());
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("could not insert new coin in this wallet");
        }
    }
    public static boolean add_coins(Wallet wallet, String shortname, double amount){
        String sqlQuery = "UPDATE COIN_IN_WALLET SET amount = ? WHERE shortname = ? AND wallet_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setDouble(1, getCoin_amount(wallet, shortname)+amount);
            preparedStatement.setString(2, shortname);
            preparedStatement.setString(3, wallet.getWallet_id());
            int insertedRows = preparedStatement.executeUpdate();

            if (insertedRows > 0) {
                System.out.printf("%f %s added to %s%n\n", amount, shortname, wallet.getWallet_id());
                return true;
            } else System.out.println("failed to add coins");
        } catch (SQLException e){
            System.out.println("failed to connect for adding coins");
        }
        return false;
    }

    public static boolean remove_coins(Wallet wallet, String shortname, double amount) {
        String sqlQuery = "UPDATE COIN_IN_WALLET SET amount = ? WHERE wallet_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setDouble(1, getCoin_amount(wallet, shortname)-amount);
            preparedStatement.setString(2, wallet.getWallet_id());

            int updated_rows = preparedStatement.executeUpdate();
            if( updated_rows > 0){
                System.out.printf("%f of %s removed from %s\n", amount, shortname, wallet.getWallet_id());
                return true;
            }
        } catch (SQLException e){
            System.out.println("could not remove coins");
        }
        return false;
    }





    public boolean update(String shortname, String wallet_id, double amount) {
        String sqlQuery = "UPDATE COIN_IN_WALLET SET amount = ?, shortname = ?, wallet_id = ? WHERE wallet_id = ? and shortname = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, shortname);
            preparedStatement.setString(3, wallet_id);
            preparedStatement.setString(4, wallet_id);
            preparedStatement.setString(5, this.shortname);


            int updated_rows = preparedStatement.executeUpdate();
            if( updated_rows > 0){
                System.out.println("coin in wallet updated");
                return true;
            }
        } catch (SQLException e){
            System.out.println("could not update coin in wallet");
        }
        return false;
    }




    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getWallet_id() {
        return this.wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }




    @Override
    public boolean remove() {
        String sqlQuery = "DELETE FROM COIN_IN_WALLET WHERE shortname = ? and wallet_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, shortname);
            preparedStatement.setString(2, this.wallet.getWallet_id());
            int deletedRows = preparedStatement.executeUpdate();

            if (deletedRows > 0){
                System.out.println("deleted Coin_in_Wallet");
                return true;
            } else System.out.println("failed to delete Coin_in_Wallet");
        } catch (SQLException e){
            System.out.println("failed to delete Coin_in_Wallet, sql exception");
        }
        return false;
    }

    @Override
    public String toString() {
        return wallet_id + "{" +
                "shortname='" + shortname + ", amount=" + amount +
                '}';
    }
}