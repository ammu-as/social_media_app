package org.example.dao;

import org.example.config.DBConnection;

import java.sql.*;

public class PasswordResetDao {
    public void createToken(int userId, String token) throws SQLException {
        String sql = "INSERT INTO password_reset (user_id, token, expires_at) VALUES (?, ?, DATE_ADD(UTC_TIMESTAMP(), INTERVAL 15 MINUTE))";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.executeUpdate();
        }
    }

    public Integer getValidUserIdByToken(String token) throws SQLException {
        String sql = "SELECT user_id FROM password_reset WHERE token = ? AND expires_at > UTC_TIMESTAMP()";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                return null;
            }
        }
    }

    public void deleteToken(String token) throws SQLException {
        String sql = "DELETE FROM password_reset WHERE token = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        }
    }
}
