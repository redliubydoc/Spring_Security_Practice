package com.raj.dto;

public class RefreshRequestDTO {
    private String refreshToken;

    public RefreshRequestDTO() {}

    public RefreshRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "LogOutRequestDTO [refreshToken=" + refreshToken + "]";
    }
}
