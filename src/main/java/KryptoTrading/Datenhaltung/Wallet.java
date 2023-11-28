package KryptoTrading.Datenhaltung;

import KryptoTrading.Fachlogik.DatabaseConnector;
import KryptoTrading.Fachlogik.HashGeneratorSHA256;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Wallet implements WalletDAO, Editable{
    private final static Connection connection = DatabaseConnector.getConnection();

    private String wallet_id;
    private User user;

    private String address;
    private String identifier_name;
    private List<Wallet> wallets;
    private final String[] GET_DATA_PARAM_LIST = {"wallet_id", "address", "identifier_name"};


    public Wallet(){
        this.user = null;
        wallet_id = "";
        identifier_name = "";
        address = "";
    }

    public static Wallet getWallet(String wallet_id) {
        Wallet wallet = new Wallet();
        String sqlQuery = "SELECT wallet_id, address, identifier_name FROM WALLET WHERE wallet_id = ?";

        // Eigentliche SQL-Abfrage
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, wallet_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    wallet.setWallet_id(resultSet.getString("wallet_id"));
                    wallet.setUser(User.getUser_byAddress(resultSet.getString("address")));
                    wallet.setAddress(resultSet.getString("address"));
                    wallet.setIdentifier_name(resultSet.getString("identifier_name"));
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("failed to connect to the wallet");
        }
        return wallet;
    }

    public String getWallet_id() {
        return wallet_id;
    }

    public User getOwner() {
        return user;
    }
    public void setIdentifier_name(String identifier_name){
        this.identifier_name = identifier_name;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getIdentifier_name() {
        return this.identifier_name;
    }

    public void setUser(User user){
        this.user = user;
        this.address = user.getAddress();
    }


    public String getAddress() {
        return this.address;
    }

    public void setWallet_id(String wallet_id){
        this.wallet_id = wallet_id;
    }
    public static Wallet getWallet(User user, String identifier_name){
        Wallet wallet = new Wallet();
        String sqlQuery = "SELECT W.wallet_id, B.address, W.identifier_name " +
                "FROM BENUTZER B JOIN WALLET W ON B.address = W.address WHERE B.username = ? AND W.identifier_name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, identifier_name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    wallet.setWallet_id(resultSet.getString("wallet_id"));
                    wallet.setUser(user);
                    wallet.setIdentifier_name(resultSet.getString("identifier_name"));
                }
            }
        } catch (SQLException e){
            System.out.println("failed to connect to the wallet");
            return null;
        }
        return wallet;
    }

    public String getData(String param) {
        String data = "";

        if (DatabaseConnector.is_valid_param(param, GET_DATA_PARAM_LIST)) {
            String sqlQuery = String.format("SELECT %s FROM WALLET WHERE wallet_id = ?", param);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, getWallet_id());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet != null && resultSet.next()) {
                        data = resultSet.getString(param);
                    }
                }
            } catch (SQLException e){
                System.out.println("failed to get data from wallet");
            }
            return data;
        } else return null;
    }

    public boolean is_existing(User user, String identifier_name){
        if (user == null){
            System.out.println("User not found. please contact admin.");
            return false;
        }
        String sqlQuery = "SELECT * FROM WALLET WHERE address = ? AND identifier_name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, user.getData("address"));
            preparedStatement.setString(2, identifier_name);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet != null && resultSet.next()){
                    String wallet_identifier_name = resultSet.getString("identifier_name");
                    if (wallet_identifier_name.equals(identifier_name)){
                        System.out.println("user already have a wallet with this name");
                        return true;
                    } else return false;
                }
            }
        } catch (SQLException e){
            System.out.println("failed to search existing wallet");
        }
        return false;
    }

    public static List<Wallet> getWallets(){
        List<Wallet> wallets = new ArrayList<>();
        String sqlQuery = "SELECT * FROM WALLET";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String wallet_id = resultSet.getString("wallet_id");
                    wallets.add(getWallet(wallet_id));
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("failed to collect wallets");
        }
        return wallets;
    }





    public static List<Wallet> getWalletsOfUser(String address){
        List<Wallet> wallets = new ArrayList<>();
        String sqlQuery = "SELECT wallet_id FROM WALLET WHERE address = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, address);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String wallet_id = resultSet.getString("wallet_id");
                    wallets.add(getWallet(wallet_id));
                }
            }
        } catch (SQLException e){
            System.out.println("failed to collect wallets");
        }
        return wallets;
    }

    @Override
    public boolean add_wallet(User user, String identifier_name) {
        if (user == null || is_existing(user, identifier_name)){
            System.out.println("Cannot add new wallet. please contact admin.");
            return false;
        }
        String wallet_id = UUID.randomUUID().toString();
        wallet_id = HashGeneratorSHA256.hashString(wallet_id);
        String sqlQuery = "INSERT INTO WALLET (wallet_id, address, identifier_name) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, wallet_id);
            preparedStatement.setString(2, user.getAddress());
            preparedStatement.setString(3, identifier_name);
            int rows_updated = preparedStatement.executeUpdate();

            if (rows_updated > 0){
                System.out.printf("wallet successfully added %s\n", identifier_name);
                return true;
            } else System.out.println("failed to add " + rows_updated + " of 3 rows");
        } catch (SQLException e) {
            System.out.println("failed to add to wallet");
        }
        return false;
    }

    @Override
    public boolean update(String username, String identifier_name) {
        String address = User.getUser(username).getAddress();
        String sqlQuery = "UPDATE WALLET SET identifier_name = ? WHERE wallet_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, identifier_name);
            preparedStatement.setString(2, this.wallet_id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                this.address =address;
                this.identifier_name = identifier_name;
                System.out.println(identifier_name + "Wallet Update succeeded");
                return true;
            } else System.out.println("Wallet update failed");
        } catch (SQLException e) {
            System.out.println("wallet update failed, sql Exception");
        }
        return false;
    }
    @Override
    public boolean delete_wallet(User user, String identifier_name) {
        if (user == null){
            System.out.println("Cannot delete wallet. pls contact admin.");
            return false;
        }

        String address = user.getData("address");
        if (!(address == null && identifier_name == null)){
            String sqlQuery = "DELETE FROM WALLET WHERE address = ? AND identifier_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, address);
                preparedStatement.setString(2, identifier_name);

                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println(identifier_name + " Wallet deleted");
                    return true;
                } else System.out.println("Wallet not found");
            } catch (SQLException e) {
                System.out.println("deleting process failed");
            }
        }
        return false;
    }

    @Override
    public boolean remove() {
       return delete_wallet(this.user, this.identifier_name);
    }
}