package org.example.model;

import java.time.LocalDateTime;

public class Login {
    private int loginId;
    private Integer userId;
    private String usernameAttempted;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime loginTime;
    private String loginStatus;
    private String reason;

    // getters/setters
    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsernameAttempted() {
        return usernameAttempted;
    }

    public void setUsernameAttempted(String usernameAttempted) {
        this.usernameAttempted = usernameAttempted;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
