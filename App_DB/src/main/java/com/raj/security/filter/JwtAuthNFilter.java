package com.raj.security.filter;

import com.raj.security.authentication.JwtAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        /**
         * If present, jwt token will be present in the
         * "Authorization" header and it will start with "Bearer "
         * authentication will fail if and only if jwt token is
         * present
         * -and it's not valid or has expired
         * on successful authentication a authentication object will be
         * created out of the details inside the jwt token and will be put
         * into the SecurityContextHolder so upcoming authentication filters will skip the
         * authentication
         * on failed authentication this filter wil simply return so the upcoming
         * filters can decide upon the authentication
         */

        /**************************************************************************
        * Authentication will fail at this filter if and only if
        * it contains a jwt token i.e a bearer token in Authentication header
        * and it's not valid
        **************************************************************************/

        String authZHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authZHeader != null && authZHeader.startsWith("Bearer ")) {

            Authentication authN = new JwtAuthentication(authZHeader.substring(7));
            authN = authenticationManager.authenticate(authN);

            if (authN != null && authN.isAuthenticated()) {

                Authentication existingAuthN = SecurityContextHolder.getContext().getAuthentication();

                if (existingAuthN != null || existingAuthN.getPrincipal().equals(authN.getPrincipal())) {
                    SecurityContextHolder.getContext().setAuthentication(authN);
                }

                filterChain.doFilter(request, response);
            }
        }
    }
}
