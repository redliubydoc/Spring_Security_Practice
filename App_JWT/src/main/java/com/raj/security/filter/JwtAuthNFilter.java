package com.raj.security.filter;

import com.raj.security.authn.JwtAuthN;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthNFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authNManager;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String authZHeader = request.getHeader("Authorization");

            if (authZHeader == null || !authZHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
            }
            else { // authorization header is there and it starts with Bearer

                Authentication authN = new JwtAuthN(
                        authZHeader.substring(7).trim(),
                        false,
                        null,
                        null
                );

                authN = authNManager.authenticate(authN);

                if (authN != null && authN.isAuthenticated()) {

                    /**
                     * Assuming this is the very first authentication filter any incoming request will encounter
                     * Therefore no need to check already authenticated or not
                     */

                    SecurityContextHolder.getContext().setAuthentication(authN);
                    filterChain.doFilter(request, response);
                }
            }
        }
        catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            response.setStatus(401);
        }
    }
}
