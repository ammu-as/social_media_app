package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnection {
    private static final Logger log = LoggerFactory.getLogger(DBConnection.class);

    // Database credentials and URL
    private static final String URL = "jdbc:mysql://localhost:3306/social_mediaa?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root123"; // <-- replace with your real password

    static {
        try {
            // Load MySQL driver (optional for modern JDBC, but safe)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("MySQL JDBC Driver not found!", e);
        }
    }

    /**
     * Get a new database connection.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Close a connection safely.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("Error closing DB connection", e);
            }
        }
    }
}
