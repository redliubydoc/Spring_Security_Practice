package com.raj.service;

import com.raj.AppConstants;
import com.raj.exception.ExpiredAccessTokenException;
import com.raj.exception.InvalidAccessTokenException;
import com.raj.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class AccessTokenService {

    public String generateAccessToken(String subject, Map<String, Object> claims) {

        String accessToken = Jwts.builder()
            .setIssuer("app_jwt_v2")
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 30000)) // 30 seconds
//            .setExpiration(new Date(System.currentTimeMillis() + 300000)) // 5 minutes
            .setSubject(subject)
            .addClaims(claims)
            .signWith(Keys.hmacShaKeyFor(AppConstants.JWT_SIGNING_KEY.getBytes()))
            .compact();

        return accessToken;
    }

    public User validateAccessToken(String accessToken)
            throws InvalidAccessTokenException, ExpiredAccessTokenException {

        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(AppConstants.JWT_SIGNING_KEY.getBytes()))
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

            return new User(
                claims.getSubject(),
                null,
                (String)claims.get("role")
            );
        }
        catch (UnsupportedJwtException
                | MalformedJwtException
                | SignatureException
                | IllegalArgumentException e) {

            throw new InvalidAccessTokenException(e);
        }
        catch (ExpiredJwtException e) {
            throw new ExpiredAccessTokenException(e);
        }
    }
}
