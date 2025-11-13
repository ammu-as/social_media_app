package org.example.dao;

import org.example.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;

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

    public Integer getValidUserIdByToken(String token) throws SQLException {
        String sql = "SELECT user_id FROM password_reset WHERE token = ? AND (expires_at IS NULL OR expires_at > NOW())";
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
