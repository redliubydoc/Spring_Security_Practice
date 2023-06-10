package com.raj.security.provider;

import com.raj.security.authn.DAOAuthN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class DAOAuthNProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Autowired
    public DAOAuthNProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername((String)authentication.getPrincipal());

            if (userDetails.getPassword().equals(authentication.getCredentials())) {
                return new DAOAuthN(
                    true,
                    userDetails.getUsername(),
                    null,
                    userDetails.getAuthorities()
                );
            }
            else {
                throw new BadCredentialsException("invalid username or password.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("invalid username or password.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (DAOAuthN.class.isAssignableFrom(authentication));
    }
}
