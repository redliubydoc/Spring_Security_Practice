package com.raj.security.filter;

import com.raj.security.constant.SecurityConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;

public class JwtGenerationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication != null) {

            String authorities = "";
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                authorities += authority.toString() + ", ";
            }

            SecretKey signingKey = Keys.hmacShaKeyFor(SecurityConstant.JWT_SIGNING_KEY.getBytes());

            String jwt = Jwts.builder()
                    .setIssuer("App_DB")
                    .claim("username", authentication.getName())
                    .claim("authorities", authorities)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                    .signWith(signingKey).compact();

            response.setHeader("Authorization", "Bearer " + jwt);
        }
    }
}
