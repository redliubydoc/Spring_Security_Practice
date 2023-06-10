package com.raj.security.authn;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class JwtAuthN implements Authentication {
    private final String jwt;
    private final boolean authenticated;
    private final String principal;
    private final List<GrantedAuthority> authorities;

    public JwtAuthN(String jwt, boolean authenticated, String principal, List<GrantedAuthority> authorities) {
        this.jwt = jwt;
        this.authenticated = authenticated;
        this.principal = principal;
        this.authorities = authorities;
    }

    public String getJwt() {
        return jwt;
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException { }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String toString() {
        return "JwtAuthN [jwt=" + jwt
                + ", authenticated=" + authenticated
                + ", principal=" + principal
                + ", authorities=" + authorities + "]";
    }
}
