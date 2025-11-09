package org.example.dao;

import org.example.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PasswordResetDao {
    public void createToken(int userId, String token, Timestamp expiresAt) throws SQLException {
        String sql = "INSERT INTO password_reset (user_id, token, expires_at) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, expiresAt);
            ps.executeUpdate();
        }
    }
}
