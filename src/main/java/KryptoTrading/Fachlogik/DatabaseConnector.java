package KryptoTrading.Fachlogik;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DatabaseConnector {
    private static final String[] param_list = {"benutzer", "coins", "coins_in_wallet", "wallet", "transaktionen"};
    private static Connection connection;

    static {
        String username = "";
        String password = "";

        String filePath = "user_data.txt";

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("username")) {
                    username = line.split(":")[1].trim();
                }
                else if (line.startsWith("password")) {
                    password = line.split(":")[1].trim();
                }
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Wrong credentials");
        }

        String url = "jdbc:oracle:thin:@172.22.160.22:1521:xe";
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                System.out.println("Connection succeed");
                System.out.println(connection);
            } else {
                System.out.println("Connection failed");
            }


        } catch (SQLException s) {
            System.out.println("An error occurred while connecting to database");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver could not be loaded", e);
        }
    }

    public static Connection getConnection(){
        return connection;
    }

    public static void getColumnNames(String table) {
        boolean valid_param = false;
        for (String s : param_list) {
            if (table.equalsIgnoreCase(s)){
                valid_param = true;
                break;
            }
        }

        if(!valid_param) {
            System.out.println("no valid parameter");
            return;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ?")) {
            preparedStatement.setString(1, table);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    System.out.println("Spalte " + i + ": " + metaData.getColumnName(i));
                }
            }
        } catch (SQLException e){
            System.out.println("failed to get table columns");
        }

    }

    public static boolean is_valid_param(String param, String[] getData_param_list){
        boolean valid_param = false;
        for (String s : getData_param_list) {
            if (param.equalsIgnoreCase(s)){
                valid_param = true;
                break;
            }
        }

        if(!valid_param) {
            System.out.println("no valid parameter");
            return false;
        }
        return true;
    }
}
