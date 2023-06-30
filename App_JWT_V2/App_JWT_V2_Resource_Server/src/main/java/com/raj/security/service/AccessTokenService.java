package com.raj.security.service;

import com.raj.AppConstants;
import com.raj.security.authentication.AccessTokenAuthN;
import com.raj.security.exception.ExpiredAccessTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


@Service
public class AccessTokenService {
    public AccessTokenAuthN buildJwtAuthNFromAccessToken(String accessToken) throws AuthenticationException {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(AppConstants.JWT_SIGNING_KEY.getBytes()))
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

            return new AccessTokenAuthN(
                true,
                claims.getSubject(),
                null,
                (String)claims.get("role")
            );
        }
        catch (UnsupportedJwtException
               | MalformedJwtException
               | SignatureException
               | IllegalArgumentException e) {

            throw new BadCredentialsException("invalid accessToken [" + accessToken + "]", e);
        }
        catch (ExpiredJwtException e) {
            throw new ExpiredAccessTokenException("expired accessToken [" + accessToken + "]", e);
        }
    }
}
