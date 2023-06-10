package com.raj.security.provider;

import com.raj.security.authn.JwtAuthN;
import com.raj.security.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthNProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SecurityConstants.JWT_SIGNING_KEY.getBytes()))
                .build()
                .parseClaimsJws(((JwtAuthN) authentication).getJwt())
                .getBody();

            return new JwtAuthN(
                null,
                true,
                claims.getSubject(),
                AuthorityUtils.createAuthorityList((String) claims.get("role")));
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("invalid jwt token.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthN.class.isAssignableFrom(authentication));
    }
}
