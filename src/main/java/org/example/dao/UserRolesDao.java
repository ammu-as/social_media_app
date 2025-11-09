package org.example.dao;

import org.example.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRolesDao {
    public void assignRole(int userId, int roleId) throws SQLException {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, roleId);
            ps.executeUpdate();
        }
    }

    public void removeRole(int userId, int roleId) throws SQLException {
        String sql = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, roleId);
            ps.executeUpdate();
        }
    }
}
