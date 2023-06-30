package com.raj.model;

import java.util.Date;

public class RefreshToken {
    private String token;
    private String userId;
    private Date expiresAt;

    public RefreshToken() {}

    public RefreshToken(String token, String userId, Date expiresAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "RefreshToken [token=" + token + ", userId=" + userId + ", expiresAt=" + expiresAt + "]";
    }
}
