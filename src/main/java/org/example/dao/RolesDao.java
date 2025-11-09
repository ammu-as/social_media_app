package org.example.dao;

import org.example.config.DBConnection;
import org.example.model.Role;

import java.sql.*;

public class RolesDao {
    public int createRole(String roleName) throws SQLException {
        String sql = "INSERT INTO roles (role_name) VALUES (?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, roleName);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public Role findByName(String name) throws SQLException {
        String sql = "SELECT * FROM roles WHERE role_name = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role r = new Role();
                    r.setRoleId(rs.getInt("role_id"));
                    r.setRoleName(rs.getString("role_name"));
                    return r;
                }
            }
        }
        return null;
    }
}
