package KryptoTrading.Datenhaltung;

import KryptoTrading.Fachlogik.DatabaseConnector;
import KryptoTrading.Fachlogik.Transferring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Editable{
    private final static Connection connection = DatabaseConnector.getConnection();
    private int transaction_id;
    private double amount;
    private String shortname;
    private int previous;
    private int next;
    private Wallet wallet_source;
    private String sourceString;
    private Wallet wallet_target;
    private String targetString;




    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTargetString(String targetString) {
        this.targetString = targetString;
    }

    public String getTargetString() {
        return this.targetString;
    }


    public String getSourceString() {
        return sourceString;
    }

    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setWallet_source(Wallet wallet_source) {
        this.wallet_source = wallet_source;
        setSourceString(wallet_source.getWallet_id());
    }

    public void setWallet_target(Wallet wallet_target) {
        this.wallet_target = wallet_target;
        setTargetString(wallet_target.getWallet_id());
    }

    public Wallet getWallet_source() {
        return wallet_source;
    }

    public Wallet getWallet_target() {
        return wallet_target;
    }

    public static Transaction getTransaktion(int id){
        Transaction transaction = new Transaction();
        String sqlQuery = "SELECT * FROM TRANSACTION WHERE transaction_id = ?";

        // Eigentliche SQL-Abfrage
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    transaction.setTransaction_id(resultSet.getInt("transaction_id"));
                    transaction.setWallet_source(Wallet.getWallet(resultSet.getString("source")));
                    transaction.setWallet_target(Wallet.getWallet(resultSet.getString("target")));
                    transaction.setShortname(resultSet.getString("shortname"));
                    transaction.setNext(resultSet.getInt("next_id"));
                    transaction.setPrevious(resultSet.getInt("previous_id"));
                    transaction.setAmount(resultSet.getFloat("amount"));
                }
            }
        } catch (SQLException e) {
            System.out.println("failed to connect to the transaction");
        }
        return transaction;
    }

    public static List<Transaction> getTransactions(){
        List<Transaction> transactions = new ArrayList<>();

        String sqlQuery = "SELECT * FROM TRANSACTION";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int transaction_id = resultSet.getInt("transaction_id");
                    String source = resultSet.getString("source");
                    String target = resultSet.getString("target");
                    String shortname = resultSet.getString("shortname");
                    int next_id = resultSet.getInt("next_id");
                    int previous_id = resultSet.getInt("previous_id");
                    double amount = resultSet.getDouble("amount");

                    Transaction transaction = getTransaktion(transaction_id);
                    transactions.add(transaction);
                    System.out.printf("%d: send %f of %s from %s to %s\n", transaction_id, amount, shortname, source, target);
                }
            }
        } catch (SQLException e){
            System.out.println("failed to collect transactions");
        }

        return transactions;
    }

    public static int get_previous_id(int transaction_id) throws SQLException{
        String sqlQuery = "SELECT previous_id FROM TRANSACTION WHERE transaction_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setInt(1, transaction_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return resultSet.getInt("previous_id");
                } else return 0;
            }

        }
    }

    public static int get_next_id(int transaction_id) throws SQLException{
        String sqlQuery = "SELECT next_id FROM TRANSACTION WHERE transaction_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setInt(1, transaction_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return resultSet.getInt("next_id");
                } else return 0;
            }
        }
    }

    public static int get_first_transactionId(){
        String sqlQuery = "SELECT MIN(transaction_id) FROM TRANSACTION";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet != null && resultSet.next()){
                    return resultSet.getInt("transaction_id");
                }
            }
        } catch (SQLException e){
            System.out.println("could not find latest transaction_id");
        }
        return -1;
    }

    public static int get_latest_transactionId(){
        String sqlQuery = "SELECT MAX(transaction_id) as max_id FROM TRANSACTION";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet != null && resultSet.next()){
                    return resultSet.getInt("max_id");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("could not find latest transaction_id");
        }
        return -1;
    }

    public static int getCurrVal() {
        String sqlQuery = "SELECT transaction_id FROM TRANSACTION ORDER BY transaction_id DESC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("transaction_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to get the latest transaction_id");
        }

        return -1;
    }

    public int getNext() {
        return next;
    }

    public int getPrevious() {
        return previous;
    }

    public String getShortname() {
        return shortname;
    }


    public boolean add(String source, String target, String shortname, int next_id, int previous_id, double amount) {
        String sqlQuery = "UPDATE TRANSAKTION SET source = ?, target = ?, shortname = ?, next_id = ?, previous_id = ?, amount = ? WHERE transaction_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, source);
            preparedStatement.setString(2, target);
            preparedStatement.setString(3, shortname);
            preparedStatement.setInt(4, next_id);
            preparedStatement.setInt(5, previous_id);
            preparedStatement.setDouble(6, amount);
            preparedStatement.setDouble(7, this.transaction_id);

            int rowsupdated = preparedStatement.executeUpdate();
            if (rowsupdated > 0) {
                this.setWallet_source(Wallet.getWallet(source));
                this.setWallet_target(Wallet.getWallet(target));
                this.setShortname(shortname);
                this.setNext(next_id);
                this.setPrevious(previous_id);
                this.setAmount(amount);
                System.out.println("Transaction updated");
                return true;
            } else System.out.println("Transaction not found");
        } catch (SQLException e) {
            System.out.println("updating Transaction failed");
        }
        return false;
    }

    public boolean update(String source, String target, String shortname, int next_id, int previous_id, double amount) {
        String sqlQuery = "UPDATE TRANSACTION SET source = ?, target = ?, shortname = ?, next_id = ?, previous_id = ?, amount = ? WHERE transaction_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, source);
            preparedStatement.setString(2, target);
            preparedStatement.setString(3, shortname);
            preparedStatement.setInt(4, next_id);
            preparedStatement.setInt(5, previous_id);
            preparedStatement.setDouble(6, amount);
            preparedStatement.setDouble(7, this.transaction_id);

            int rowsupdated = preparedStatement.executeUpdate();
            if (rowsupdated > 0) {
                this.setWallet_source(Wallet.getWallet(source));
                this.setWallet_target(Wallet.getWallet(target));
                this.setShortname(shortname);
                this.setNext(next_id);
                this.setPrevious(previous_id);
                this.setAmount(amount);
                System.out.println("Transaction updated");
                return true;
            } else System.out.println("Transaction not found");
        } catch (SQLException e) {
            System.out.println("updating Transaction failed");
        }
        return false;
    }

    @Override
    public boolean remove() {
        /*
        String sqlQuery = "DELETE FROM TRANSACTION WHERE transaction_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, transaction_id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Transaction deleted");
                return true;
            } else System.out.println("Transaction not found");
        } catch (SQLException e) {
            System.out.println("deleting Transaction failed");
        }
        return false;

         */
        return Transferring.remove_transaction(this.transaction_id, this);
    }
}