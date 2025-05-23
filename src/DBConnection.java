import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "librarydb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        // First connect without database name
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        
        // Create database if it doesn't exist
        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        stmt.close();
        conn.close();
        
        // Now connect to the specific database
        return DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
    }
}