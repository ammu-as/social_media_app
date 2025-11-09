package org.example.dao;

import org.example.config.DBConnection;
import org.example.model.user;

import java.sql.*;

public class UserDao {
    public int create(user u) throws SQLException {
        String sql = "INSERT INTO users (username, mobile_no, mail_id, status, hash_password) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getMobileNo());
            ps.setString(3, u.getMailId());
            ps.setString(4, u.getStatus());
            ps.setString(5, u.getHashPassword());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public user findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user u = new user();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setMailId(rs.getString("mail_id"));
                    u.setMobileNo(rs.getString("mobile_no"));
                    u.setStatus(rs.getString("status"));
                    u.setHashPassword(rs.getString("hash_password"));
                    return u;
                }
            }
        }
        return null;
    }

    public user findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE mail_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user u = new user();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setMailId(rs.getString("mail_id"));
                    u.setMobileNo(rs.getString("mobile_no"));
                    u.setStatus(rs.getString("status"));
                    u.setHashPassword(rs.getString("hash_password"));
                    return u;
                }
            }
        }
        return null;
    }

    public user findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user u = new user();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setMailId(rs.getString("mail_id"));
                    u.setMobileNo(rs.getString("mobile_no"));
                    u.setStatus(rs.getString("status"));
                    u.setHashPassword(rs.getString("hash_password"));
                    return u;
                }
            }
        }
        return null;
    }
}
