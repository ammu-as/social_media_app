package org.example.model;
import java.time.LocalDateTime;
public class Friend {
    private int followerId;
    private int followingId;
    private String status;
    private LocalDateTime createdAt;
    // getters/setters
    public int getFollowerId() { return followerId; }
    public void setFollowerId(int followerId) { this.followerId = followerId; }
    public int getFollowingId() { return followingId; }
    public void setFollowingId(int followingId) { this.followingId = followingId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
