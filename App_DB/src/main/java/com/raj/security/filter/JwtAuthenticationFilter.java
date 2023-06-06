package com.raj.security.filter;

import com.raj.security.constant.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collection;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
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

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            try {
                // TODO: Validate token by checking it's signature

                /**
                 * if the token is valid extract required details from it
                 * create an authentication token out of it
                 * put it in the security context holder
                 */

                SecretKey signingKey = Keys.hmacShaKeyFor(SecurityConstant.JWT_SIGNING_KEY.getBytes());

                Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

                String username = (String) claims.get("username");
                String authorities = (String) claims.get("authorities");

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (Exception e) {
                throw new BadCredentialsException("Invalid jwt token.");
            }
        }

        filterChain.doFilter(request, response);
    }
}
