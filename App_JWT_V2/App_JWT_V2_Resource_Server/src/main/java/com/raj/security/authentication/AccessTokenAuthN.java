package com.raj.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class AccessTokenAuthN implements Authentication {

    private final boolean authenticated;
    private final String principal;
    private final String accessToken;
    private final String role;

    public AccessTokenAuthN(boolean authenticated, String principal, String accessToken, String role) {
        this.authenticated = authenticated;
        this.principal = principal;
        this.accessToken = accessToken;
        this.role = role;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public String getName() {
        return (String)getPrincipal();
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Object getCredentials() {
        return getAccessToken();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role);
    }

    @Override
    public Object getDetails() {
        return null;
    }
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException { }
}
