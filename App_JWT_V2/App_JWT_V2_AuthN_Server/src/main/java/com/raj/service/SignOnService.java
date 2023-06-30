package com.raj.service;

import com.raj.dao.RefreshTokenDAO;
import com.raj.dao.UserDAO;
import com.raj.exception.BadCredentialsException;
import com.raj.exception.RefreshTokenGenerationException;
import com.raj.exception.UserNotFoundException;
import com.raj.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class SignOnService {
    private final UserDAO userDAO;
    private final RefreshTokenDAO refreshTokenDAO;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public SignOnService(UserDAO userDAO, RefreshTokenDAO refreshTokenDAO, AccessTokenService accessTokenService, RefreshTokenService refreshTokenService) {
        this.userDAO = userDAO;
        this.refreshTokenDAO = refreshTokenDAO;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    public Map<String, Object> doSignOn(String username, String password)
            throws Exception {

        User user = userDAO.getUserByUserId(username).orElse(null);

        if (user == null) throw new UserNotFoundException("[" + username + "] doesn't exists.") ;
        if (!user.getPassword().equals(password)) throw new BadCredentialsException("invalid password for [" + username + "]");

        /**
         * Reaching this point implies authentication is successful i.e. username and password is valid
         */
        String accessToken = accessTokenService.generateAccessToken(user.getUserId(), Map.of("role", user.getRole()));
        String refreshToken = refreshTokenService.generateRefreshToken(user.getUserId());

        return Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken,
            "userId", user.getUserId(),
            "role", user.getRole()
        );
    }
}
