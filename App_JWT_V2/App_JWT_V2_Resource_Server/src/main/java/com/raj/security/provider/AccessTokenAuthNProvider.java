package com.raj.security.provider;

import com.raj.security.authentication.AccessTokenAuthN;
import com.raj.security.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenAuthNProvider implements AuthenticationProvider {
    private final AccessTokenService accessTokenService;

    @Autowired
    public AccessTokenAuthNProvider(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        return accessTokenService.buildJwtAuthNFromAccessToken(
            ((AccessTokenAuthN)authentication).getAccessToken()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccessTokenAuthN.class.isAssignableFrom(authentication);
    }
}
