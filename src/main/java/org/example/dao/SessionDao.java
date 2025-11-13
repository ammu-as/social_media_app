package org.example.dao;

import org.example.config.DBConnection;

import java.sql.*;

public class SessionDao {
    public void createSession(String sessionId, int userId, Timestamp expiresAt, String ip, String userAgent) throws SQLException {
        String sql = "INSERT INTO sessions (session_id, user_id, login_time, expires_at, ip_address, user_agent, is_active) VALUES (?, ?, NOW(), ?, ?, ?, 1)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            ps.setInt(2, userId);
            ps.setTimestamp(3, expiresAt);
            ps.setString(4, ip);
            ps.setString(5, userAgent);
            ps.executeUpdate();
        }
    }

    public void invalidate(String sessionId) throws SQLException {
        String sql = "UPDATE sessions SET is_active = 0, logout_time = NOW() WHERE session_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            ps.executeUpdate();
        }
    }

    public Integer getUserIdBySession(String sessionId) throws SQLException {
        String sql = "SELECT user_id FROM sessions WHERE session_id = ? AND is_active = 1";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                return null;
            }
        }
    }
}
