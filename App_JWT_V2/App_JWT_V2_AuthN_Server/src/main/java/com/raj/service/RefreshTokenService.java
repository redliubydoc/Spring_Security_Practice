package com.raj.service;

import com.raj.dao.RefreshTokenDAO;
import com.raj.exception.InvalidRefreshTokenException;
import com.raj.exception.RefreshTokenGenerationException;
import com.raj.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenDAO refreshTokenDAO;

    @Autowired
    public RefreshTokenService(RefreshTokenDAO refreshTokenDAO) {
        this.refreshTokenDAO = refreshTokenDAO;
    }

    public String generateRefreshToken(String userId) throws Exception {
        /**
         * Generate globally unique refresh token
         * There could be at most certain number of refreshToken per user.
         */
        String token = UUID.randomUUID().toString();
        while (refreshTokenDAO.findByToken(token).isPresent()) {
            token = UUID.randomUUID().toString();
        }

        RefreshToken refreshToken = new RefreshToken(token, userId, new Date(System.currentTimeMillis() + 900000)); // 15 minutes
        if (!refreshTokenDAO.addToken(refreshToken)) {
            throw new Exception("cannot add refreshToken to DB");
        }

        System.out.println(refreshToken);
        return token;
    }

    public void revokeRefreshToken(RefreshToken refreshToken) throws Exception {
        if (!refreshTokenDAO.deleteToken(refreshToken)) {
            throw new Exception("cannot delete refreshToken from DB");
        }
    }

    public RefreshToken validateRefreshToken(String token) throws InvalidRefreshTokenException {
        RefreshToken refreshToken = refreshTokenDAO.findByToken(token).orElse(null);

        if (refreshToken == null || refreshToken.getExpiresAt().getTime() < System.currentTimeMillis()) {
            throw new InvalidRefreshTokenException();
        }

        return refreshToken;
    }

    @Scheduled(initialDelay = 900000, fixedRate = 900000) // 15 minutes
    public void deleteAllExpiredRefreshTokens() {
        if (refreshTokenDAO.deleteAllExpired()) {
            System.out.println("cleaned up expired tokens");
        }
        else {
            System.out.println("cannot clean up expired tokens");
        }
    }
}
