package KryptoTrading.Datenhaltung;

import KryptoTrading.Fachlogik.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User implements Editable {
    private String username;
    private final static Connection connection = DatabaseConnector.getConnection();
    private final String[] getData_param_list = {"address", "username", "password"};
    private String address;
    private String passw;


    public User() {
        username = "";
        address = "";
        passw = "";
    }

    public String getAddress() {
        return address;
    }

    public String getPassw() {
        return passw;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username){this.username = username;}

    public void setAddress(String address) {this.address = address;}

    public void setPassw(String passw) {
        this.passw = passw;

    }



    public static User getUser(String username) {
        User user = new User();
        String sqlQuery = "SELECT ADDRESS, USERNAME, PASSWORD FROM \"USER\" WHERE USERNAME = ?";

        // Eigentliche SQL-Abfrage
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    user.setAddress(resultSet.getString("address"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassw(resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.out.println("failed to connect to the user");
        }
        return user;
    }

    public static User getUser_byAddress(String address) {
        User user = new User();
        String sqlQuery = "SELECT ADDRESS, USERNAME, PASSWORD FROM \"USER\" WHERE address = ?";

        // Eigentliche SQL-Abfrage
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, address);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    user.setUsername(resultSet.getString("username"));
                    user.setAddress(resultSet.getString("address"));
                    user.setPassw(resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.out.println("failed to connect to the user");
        }
        return user;
    }

    public String getData(String param) {
        String data = null;
        if (DatabaseConnector.is_valid_param(param, getData_param_list)) {
            String sqlQuery = String.format("SELECT %s FROM \"USER\" WHERE username = ?", param);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet != null && resultSet.next()) {
                        data = resultSet.getString(param);
                    }
                }
            } catch (SQLException e) {
                System.out.println("failed to get data from user");
            }
            return data;
        } else return null;
    }

    public static List<User> getUser(){
        List<User> userList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM \"USER\"";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String address = resultSet.getString("address");

                    User user = User.getUser(username);
                    userList.add(user);
                    System.out.println(user.getData("username") + " added to list");
                }
            }
        } catch (SQLException e){
            System.out.println("failed to collect user");
        }

        return userList;
    }

    public static List<Wallet> wallets_of_user(User user){
        user = User.getUser(user.getData("username"));
        List<Wallet> wallets = new ArrayList<>();

        String sqlQuery = "SELECT * FROM WALLET WHERE adresse = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, user.getData("address"));
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String wallet_id = resultSet.getString("wallet_id");
                    String adresse = resultSet.getString("address");
                    String identifier_name = resultSet.getString("identifier_name");

                    Wallet wallet = Wallet.getWallet(wallet_id);
                    wallets.add(wallet);
                    System.out.println(identifier_name + " by " + adresse + " successfully added");
                }
            }
        } catch (SQLException e){
            System.out.println("failed to collect wallets of " + user.getData("username"));
        }
        return wallets;
    }

    public static boolean register(String username, String address, String hashed_password) throws SQLException{
        String sqlQuery = "INSERT INTO \"USER\" (username, password, address) VALUES (?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashed_password);
            preparedStatement.setString(3, address);
            int rows_updated = preparedStatement.executeUpdate();

            if (rows_updated > 0) {
                System.out.println("register successful");
                return true;
            } else System.out.println("failed to register " + rows_updated + " of 3 rows");
        }
        return false;
    }

    public static boolean login(String address, String hashed_password) {
        String sqlQuery = "SELECT password, address FROM \"USER\" WHERE password = ? AND address = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, hashed_password);
            preparedStatement.setString(2, address);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                boolean login =  resultSet.next();
                resultSet.close();
                System.out.println("login: " + login);
                return login;
            }
        } catch (SQLException e) {
            System.out.println("failed to login to user");
        }
        return false;
    }

    public boolean update(String username) {
        String sqlQuery = "UPDATE \"USER\" Set username = ? WHERE address = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, this.getAddress());
            int rowcount = preparedStatement.executeUpdate();
            if (rowcount > 0) {
                System.out.println("updated User successful");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("failed to login to user");
        }
        return false;
    }

    @Override
    //TODO: für bearbeitung aus dem adminbereich
    public boolean remove() {
        String sqlQuery = "DELETE FROM \"USER\" WHERE  address = ?";

        // Eigentliche SQL-Abfrage
        try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);) {
            preparedStatement.setString(1, this.address);
            int rows_updated = preparedStatement.executeUpdate();
            if (rows_updated > 0) {
                System.out.println("deleted successfully");
                return true;
            } else System.out.println("failed to delete");
        } catch (SQLException e) {
            System.out.println("failed to delete");
        }
        return false;
    }
}