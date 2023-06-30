package com.raj.service;

import com.raj.model.RefreshToken;
import com.raj.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogOutService {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public LogOutService(AccessTokenService accessTokenService, RefreshTokenService refreshTokenService) {
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Logout will only happen if both accessToken and refreshToken is valid
     * On logout the refreshToken will be revoked i.e. deleted from the database
     * Steps --
     * validate accessToken
     * validate refreshToken
     * revoke refreshToken
     */
    public void doLogOut(
            String accessToken,
            String refreshToken
    ) throws Exception {

        accessTokenService.validateAccessToken(accessToken);
        RefreshToken refreshTokenObj = refreshTokenService.validateRefreshToken(refreshToken);
        refreshTokenService.revokeRefreshToken(refreshTokenObj);
    }
}
