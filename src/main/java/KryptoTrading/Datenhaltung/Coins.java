package KryptoTrading.Datenhaltung;

import KryptoTrading.Fachlogik.CoinFunctions;
import KryptoTrading.Fachlogik.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Coins implements Editable {
    private final static Connection connection = DatabaseConnector.getConnection();
    private final String[] getData_param_list = {"shortname" ,"name" ,"value" ,"market_capitalization" ,"amount"};

    private String name;
    private String shortname;
    private long total_amount;
    private double value;
    private double market_capitalization;

    public Coins(){
        shortname = "";
        name = "";
        value = 0;
        market_capitalization = 0;
        total_amount = 0;
    }

    public Coins(String name, String shortname, long total_amount, double value, double market_capitalization) {
        this.name = name;
        this.shortname = shortname;
        this.total_amount = total_amount;
        this.value = value;
        this.market_capitalization = market_capitalization;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName(){return this.name;}


    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getShortname() {
        return this.shortname;
    }

    public void setMarket_capitalization(double market_capitalization) {
        this.market_capitalization = market_capitalization;
    }

    public double getMarket_capitalization() {
        return this.market_capitalization;
    }

    public void setTotal_amount(long total_amount) {
        this.total_amount = total_amount;
    }

    public long getTotal_amount() {
        return this.total_amount;
    }


    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public static Coins getCoin(String shortname){
        Coins coin = new Coins();
        String sqlQuery = "SELECT * FROM COINS WHERE shortname = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, shortname);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    System.out.println("iff");
                    coin.setShortname(resultSet.getString("shortname"));
                    coin.setName(resultSet.getString("name"));
                    coin.setValue(resultSet.getDouble("value"));
                    coin.setMarket_capitalization(resultSet.getDouble("market_capitalization"));
                    coin.setTotal_amount(resultSet.getLong("total_amount"));
                } else return null;
            }
        } catch (SQLException e) {
            System.out.println("failed to connect to the coin");
        }
        return coin;
    }

    public String getData(String param) {
        String data = null;
        if (DatabaseConnector.is_valid_param(param, getData_param_list)) {
            // Allgemeiner SQL-Befehl als String
            String sqlQuery = String.format("SELECT %s FROM COINS WHERE name = ?", param);

            // Eigentliche SQL-Abfrage
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, name);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet !=  null && resultSet.next()) {
                        data = resultSet.getString(param);
                    }
                }
            } catch (SQLException e) {
                System.out.println("failed to get data from coin");
            }
            return data;
        } else return null;
    }

    public static List<Coins> getCoins(){
        List<Coins> coins = new ArrayList<>();
        String sqlQuery = "SELECT * FROM COINS";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String shortname = resultSet.getString("shortname");
                    String name = resultSet.getString("name");
                    double value = resultSet.getDouble("value");
                    long total_amount = resultSet.getLong("total_amount");
                    double market_capitalization = resultSet.getDouble("market_capitalization");
                    Coins coin = new Coins();
                    coin.setName(name);
                    coin.setValue(value);
                    coin.setTotal_amount(total_amount);
                    coin.setShortname(shortname);
                    coin.setMarket_capitalization(market_capitalization);
                    System.out.println(coin);
                    coins.add(coin);
                    System.out.println(shortname + ": " + name + " successfully added");
                }
            }
        } catch (SQLException e){
            System.out.println("failed to collect coins");
        }
        return coins;
    }



    public static boolean add(String shortname, String name, double value, double market_capitalization, int total_amount) {
        if (getCoin(name) == null) {
            String sqlQuery = "INSERT INTO COINS (shortname, name, value, market_capitalization, total_amount) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, shortname);
                preparedStatement.setString(2, name);
                preparedStatement.setDouble(3, value);
                preparedStatement.setDouble(4, market_capitalization);
                preparedStatement.setInt(5, total_amount);
                int rows_updated = preparedStatement.executeUpdate();

                if (rows_updated > 0) {
                    System.out.println("coin " + name + " successfully added");
                    return true;
                } else System.out.println("failed to add " + rows_updated + " of 5 rows");
            } catch (SQLException e) {
                System.out.println("failed to add new coin");
                e.printStackTrace();
            }
        }
        return false;
    }


    public boolean update(String name, double value, double market_kapitalization, long total_amount) {
        System.out.println("shortname=" + shortname + ", name=" + name + ", value=" + value + ", marktkap=" + market_kapitalization + ", total_amount=" + total_amount + ". echtes kuerzel=" + this.shortname);
        boolean updated = true;
        //if (getCoin(name) == null) {
            String sqlQuery = "UPDATE COINS SET name = ?, value = ?, market_capitalization = ?, total_amount = ? WHERE shortname = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, name);
                preparedStatement.setDouble(2, value);
                preparedStatement.setDouble(3, market_kapitalization);
                preparedStatement.setLong(4, total_amount);
                preparedStatement.setString(5, this.shortname);
                int rows_updated = preparedStatement.executeUpdate();
                if (rows_updated > 0 ) {
                    this.shortname = shortname;
                    this.value = value;
                    this.market_capitalization = market_kapitalization;
                    this.name = name;
                    this.total_amount = total_amount;
                    System.out.println("update Coin succeeded");
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("failed to update coin");
                e.printStackTrace();
            }
        //}
        System.out.println("update Coin failed");
        return false;
    }

    public static boolean remove (String shortname) {
        String sqlQuery = "DELETE FROM COINS WHERE shortname = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, shortname);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Coin deleted");
                return true;
            } else System.out.println("Coin not found");
        } catch (SQLException e) {
            System.out.println("deleting Coin failed");
        }

        return false;
    }


    @Override
    public boolean remove() {
       return CoinFunctions.remove_Coin(this.getShortname());

    }

}