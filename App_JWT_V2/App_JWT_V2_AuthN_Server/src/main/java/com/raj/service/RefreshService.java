package com.raj.service;

import com.raj.dao.UserDAO;
import com.raj.exception.UserNotFoundException;
import com.raj.model.RefreshToken;
import com.raj.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RefreshService {

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final UserDAO userDAO;

    @Autowired
    public RefreshService(RefreshTokenService refreshTokenService, AccessTokenService accessTokenService, UserDAO userDAO) {
        this.refreshTokenService = refreshTokenService;
        this.accessTokenService = accessTokenService;
        this.userDAO = userDAO;
    }

    public Map<String, Object> doRefresh(String refreshToken)
            throws Exception {

        /**
         * Validate refresh
         * Revoke old refreshToken
         * Generate new accessToken and refreshToken
         */

        RefreshToken refreshTokenObj = refreshTokenService.validateRefreshToken(refreshToken);
        refreshTokenService.revokeRefreshToken(refreshTokenObj);
        User user = userDAO.getUserByUserId(refreshTokenObj.getUserId()).orElse(null);

        if (user == null) {
            throw new UserNotFoundException("[" + refreshTokenObj.getUserId() + "] doesn't exists.");
        }

        String userId = user.getUserId();
        String role = user.getRole();

        String newAccessToken = accessTokenService.generateAccessToken(userId, Map.of("role", user.getRole()));
        String newRefreshToken = refreshTokenService.generateRefreshToken(userId);

        return Map.of(
            "accessToken", newAccessToken,
            "refreshToken", newRefreshToken,
            "userId", userId,
            "userRole", role
        );
    }
}
