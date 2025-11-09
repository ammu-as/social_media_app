package org.example.dao;

import org.example.config.DBConnection;

import java.sql.*;

public class LoginDao {
    public void recordLogin(Integer userId, String usernameAttempted, String ip, String userAgent, String status, String reason) throws SQLException {
        String sql = "INSERT INTO login (user_id, username_attempted, ip_address, user_agent, login_status, reason) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (userId != null) ps.setInt(1, userId);
            else ps.setNull(1, Types.INTEGER);
            ps.setString(2, usernameAttempted);
            ps.setString(3, ip);
            ps.setString(4, userAgent);
            ps.setString(5, status);
            ps.setString(6, reason);
            ps.executeUpdate();
        }
    }
}
