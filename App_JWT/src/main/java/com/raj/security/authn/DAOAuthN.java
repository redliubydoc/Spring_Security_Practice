package com.raj.security.authn;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class DAOAuthN implements Authentication {

    private final boolean authenticated;
    private final String principal;
    private final String credentials;
    private final Collection<? extends GrantedAuthority> authorities;

    public DAOAuthN(boolean authenticated, String principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        this.authenticated = authenticated;
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public String getName() {
        return getPrincipal();
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException { }

    @Override
    public String toString() {
        return "UsernamePasswordAuthN [authenticated=" + authenticated
                + ", principal=" + principal
                + ", credentials=" + credentials
                + ", authorities=" + authorities + "]";
    }
}
