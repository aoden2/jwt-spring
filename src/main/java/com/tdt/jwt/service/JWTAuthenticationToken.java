package com.tdt.jwt.service;

import com.tdt.jwt.model.UserAuth;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * @author
 **/
public class JWTAuthenticationToken implements Authentication {

    protected String token;
    private UserAuth userAuth;
    protected boolean isAuthenticated = false;
    ApplicationContext ctx;

    public JWTAuthenticationToken(String token, ApplicationContext ctx) {
        this.token = token;
        this.ctx = ctx;
        this.userAuth = ctx.getBean(JWTService.class).parseUserFromToken(token);
    }

    public JWTAuthenticationToken() {
    }

    public JWTAuthenticationToken(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userAuth;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

}
