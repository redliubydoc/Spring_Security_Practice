package com.raj.controller;

import com.raj.model.dto.SignOnRequestDTO;
import com.raj.security.authn.DAOAuthN;
import com.raj.security.constant.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(path = "/public/authn")
public class SignOnController {
    private final AuthenticationManager authNManager;

    @Autowired
    public SignOnController(AuthenticationManager authNManager) {
        this.authNManager = authNManager;
    }

    @PostMapping(path = "/so")
    public ResponseEntity<Map<String, Object>> signOn(@RequestBody SignOnRequestDTO request) {

        ResponseEntity<Map<String, Object>> response = null;

        String username = request.getUsername();
        String password = request.getPassword();

        if (username.isEmpty() || password.isEmpty()) {

            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("endpoint", "endpoint @ public");
            responseBody.put("is_success", false);
            responseBody.put("reason", "username or password is empty");
            responseBody.put("authentication", SecurityContextHolder.getContext().getAuthentication().toString());

            response = ResponseEntity.badRequest().body(responseBody);
        }
        else {
            try {
                Authentication authN = new DAOAuthN(false, username, password, null);
                authN = authNManager.authenticate(authN);

                if (authN != null && authN.isAuthenticated()) {

                    String jwt = Jwts.builder()
                        .setIssuer("app_jwt")
                        .setSubject( authN.getName())
                        .claim("role", new ArrayList<>(authN.getAuthorities()).get(0).toString())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                        .signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_SIGNING_KEY.getBytes()))
                        .compact();

                    Map<String, Object> responseBody = new LinkedHashMap<>();
                    responseBody.put("endpoint", "endpoint @ public");
                    responseBody.put("is_success", true);
                    responseBody.put("jwt", jwt);
                    responseBody.put("authentication", SecurityContextHolder.getContext().getAuthentication().toString());
                    response = ResponseEntity.ok().body(responseBody);
                }
            }
            catch (AuthenticationException e) {
                e.printStackTrace();

                Map<String, Object> responseBody = new LinkedHashMap<>();
                responseBody.put("endpoint", "endpoint @ public");
                responseBody.put("is_success", false);
                responseBody.put("reason", "authentication exception :: " + e.getMessage());
                responseBody.put("authentication", SecurityContextHolder.getContext().getAuthentication().toString());
                response = ResponseEntity.badRequest().body(responseBody);
            }
        }

        return response;
    }
}
