package com.raj.security.filter;

import com.raj.AppConstants;
import com.raj.security.authentication.AccessTokenAuthN;
import com.raj.security.exception.ExpiredAccessTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AccessTokenAuthNFilter extends OncePerRequestFilter {

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
            else { // Authorization header is there and it starts with Bearer
                String accessToken = authZHeader.substring(7).trim();
                Authentication authN = new AccessTokenAuthN(false, null, accessToken, null);
                authN = authNManager.authenticate(authN);

                if (authN != null && authN.isAuthenticated()) {
                    /**
                     * Assuming this is the very first authentication filter any incoming request will encounter
                     * Therefore no need to check already authenticated or not
                     */
                    SecurityContextHolder.getContext().setAuthentication(authN);
                    filterChain.doFilter(request, response);
                }
                else {
                    throw new BadCredentialsException("authentication failed.");
                }
            }
        }
        catch (ExpiredAccessTokenException e) {
            e.printStackTrace();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(401);
            response.getWriter().print(
                "{\n" +
                "    \"isError\": true,\n" +
                "    \"error\": {\n" +
                "        \"code\": " + AppConstants.ERR_CODE_EXPIRED_ACCESS_TOKEN + ",\n" +
                "        \"msg\": \"expired accessToken\"\n" +
                "    }\n" +
                "}"
            );
        }
        catch (AuthenticationException e) {
            e.printStackTrace();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(401);
            response.getWriter().print(
                "{\n" +
                "    \"isError\": true,\n" +
                "    \"error\": {\n" +
                "        \"code\": " + AppConstants.ERR_CODE_INVALID_ACCESS_TOKEN + ",\n" +
                "        \"msg\": \"invalid accessToken\"\n" +
                "    }\n" +
                "}"
            );
        }
        catch (Exception e) {
            e.printStackTrace();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(500);
        }
    }
}
