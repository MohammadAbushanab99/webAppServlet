package webapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gradingsys";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("connected");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);


            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void main(String[] args) {
        getConnection();
    }

}
