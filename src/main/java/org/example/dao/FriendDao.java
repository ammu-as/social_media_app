package org.example.dao;

import org.example.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FriendDao {
    public boolean follow(int followerId, int followeeId) throws SQLException {
        String sql = "INSERT INTO friends (follower_id, following_id, status) VALUES (?, ?, 'active')";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, followerId);
            ps.setInt(2, followeeId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void unfollow(int followerId, int followeeId) throws SQLException {
        String sql = "DELETE FROM friends WHERE follower_id = ? AND following_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, followerId);
            ps.setInt(2, followeeId);
            ps.executeUpdate();
        }
    }

    public List<Integer> findFollowees(int followerId) throws SQLException {
        String sql = "SELECT following_id FROM friends WHERE follower_id = ? AND status = 'active'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, followerId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Integer> out = new ArrayList<>();
                while (rs.next()) out.add(rs.getInt(1));
                return out;
            }
        }
    }
}
