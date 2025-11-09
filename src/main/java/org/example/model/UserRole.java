package org.example.model;
import java.time.LocalDateTime;
public class UserRole {
    private int userId;
    private int roleId;
    private LocalDateTime assignedAt;
    // getters/setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
