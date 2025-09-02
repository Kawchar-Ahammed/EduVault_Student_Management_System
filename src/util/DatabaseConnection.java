package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for database operations
 */
public class DatabaseConnection {
    // Database connection parameters
    private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-3NT5988\\SQLEXPRESS;databaseName=PushpoAcademicDB;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";
    //private static final String USER = "sa";
   // private static final String PASSWORD = "YourPassword"; // Change this to your actual password
    
    // Get database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            // Open a connection
            Connection conn = DriverManager.getConnection(DB_URL);
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found: " + e.getMessage());
            throw new SQLException("SQL Server JDBC Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw new SQLException("Connection failed: " + e.getMessage(), e);
        }
    }
    
    // Close resources with enhanced error handling
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    System.err.println("Error closing resource: " + e.getMessage());
                }
            }
        }
    }
}