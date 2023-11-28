package KryptoTrading.Fachlogik;

import KryptoTrading.Datenhaltung.Transaction;
import KryptoTrading.Datenhaltung.Wallet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transferring {
    private static final Connection connection = DatabaseConnector.getConnection();
    private double amount;
    private String shortname;
    private Wallet source;
    private Wallet target;

    private int setId(String sqlQuery){
        int result = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet != null && resultSet.next()){
                    result = resultSet.getInt("transaction_id");
                }
            }
        } catch (SQLException e){
            System.out.println("can't get actual transaction_id");
        }
        return result;
    }

    public static boolean update_transfer(){
        String sqlQuery = "UPDATE TRANSACTION SET next_id = ? WHERE transaction_id = ?";
        int curr_val;
        if ((curr_val = Transaction.getCurrVal()) != -1){
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
                preparedStatement.setInt(1, curr_val);
                preparedStatement.setInt(2, curr_val-1);
                int updated_rows = preparedStatement.executeUpdate();

                if (updated_rows > 0){
                    System.out.println("update transfer succeed");
                    return true;
                } else System.out.println("updating failed");
            } catch (SQLException e){
                System.out.println("failed to update");
            }
        }
        return false;
    }

    public static void clean_up_failed_transfer(){
        String sqlQuery = "DELETE FROM TRANSACTION WHERE transaction_id = ?";
        String sqlUpdate = "UPDATE TRANSACTION SET next_id = NULL WHERE transaction_id = ?";
        int curr_val = Transaction.getCurrVal();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)){
            preparedStatement.setInt(1, curr_val);
            updateStatement.setInt(1, curr_val-1);
            int deleted_rows = preparedStatement.executeUpdate();
            int updated_rows = updateStatement.executeUpdate();
            if(deleted_rows > 0 && updated_rows > 0){
                System.out.println("clean up successful");
            } else System.out.println("clean up failed");
        } catch (SQLException e){
            System.out.println("failed to clean up");
        }
    }

    public static void transfer_coins(String shortname, double amount, Wallet source, Wallet target) {
        if (source == null || target == null){
            return;
        }
        System.out.println(source.getIdentifier_name());
        System.out.println(target.getIdentifier_name());
        if (CoinFunctions.remove_coins_from_wallet(shortname, amount, source) && CoinFunctions.add_coins_to_wallet(shortname, amount, target)) {
            String sqlQuery = "INSERT INTO TRANSACTION (next_id, previous_id, transaction_id, source, target, shortname, amount) " +
                    "VALUES (NULL, ?, transaction_seq.NEXTVAL, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(sqlQuery)) {
                insertStatement.setInt(1, Transaction.get_latest_transactionId());
                insertStatement.setString(2, source.getData("wallet_id"));
                insertStatement.setString(3, target.getData("wallet_id"));
                insertStatement.setString(4, shortname);
                insertStatement.setDouble(5, amount);
                int inserted_rows = insertStatement.executeUpdate();
                if (inserted_rows > 0 && update_transfer()) {
                    System.out.println("transfer successful");
                } else {
                    System.out.println("failed to transfer coins");
                    clean_up_failed_transfer();
                }
            } catch (SQLException e) {
                System.out.println("transfer failed");
            }
        }
    }

    //TODO neue Verkettung der LinkedList
    public static boolean remove_transaction(int transaction_id, Transaction transaction){
        Transaction rem = Transaction.getTransaktion(transaction_id);
        Transaction prev = Transaction.getTransaktion(rem.getPrevious());
        Transaction next = Transaction.getTransaktion(rem.getNext());
        String sqlUpdate1 = "UPDATE TRANSACTION SET next_id = ? WHERE transaction_id = ?";
        String sqlUpdate2 = "UPDATE TRANSACTION SET previous_id = ? WHERE transaction_id = ?";
        String sqlQuery = "DELETE FROM TRANSACTION WHERE transaction_id = ?";
        int curr_val = Transaction.getCurrVal();
        try (PreparedStatement preparedStatement1 = connection.prepareStatement(sqlUpdate1);
        PreparedStatement preparedStatement2 = connection.prepareStatement(sqlUpdate2);
        PreparedStatement deleteStatement = connection.prepareStatement(sqlQuery)){

            System.out.println("prev bekommt als next die " + next.getTransaction_id());


            preparedStatement1.setInt(1, next.getTransaction_id());
            preparedStatement1.setInt(2, prev.getTransaction_id());

            preparedStatement2.setInt(1, prev.getTransaction_id());
            preparedStatement2.setInt(2, next.getTransaction_id());

            deleteStatement.setInt(1, rem.getTransaction_id());

            int updated_rows1 = -1;
            int updated_rows2 = -1;
            int updated_rows3 = -1;

            if (rem != null) updated_rows3 = deleteStatement.executeUpdate();
            if (prev != null) updated_rows1 = preparedStatement1.executeUpdate();
            if (next != null) updated_rows2 = preparedStatement2.executeUpdate();


            if((updated_rows1 > 0 || updated_rows1 == -1) && (updated_rows2 > 0 || updated_rows2 == -1) && (updated_rows3 > 0 || updated_rows3 == -1)){
                System.out.println("removed transaction and refreshed linking");
                return true;
            } else{
                System.out.println("removing transaction failed");
                return false;
            }
        } catch (SQLException e){
            System.out.println("removing transaction failed, sql exception");
            e.printStackTrace();
            return false;
        }

    }
}
