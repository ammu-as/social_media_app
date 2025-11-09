package org.example.dao;

import org.example.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterDao {
    public void recordRegister(int userId, String method, String ip, String referrer) throws SQLException {
        String sql = "INSERT INTO register (user_id, method, ip_address, referrer) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, method);
            ps.setString(3, ip);
            ps.setString(4, referrer);
            ps.executeUpdate();
        }
    }
}
