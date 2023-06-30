package com.raj.security.exception;

import org.springframework.security.core.AuthenticationException;

public class ExpiredAccessTokenException extends AuthenticationException {

    public ExpiredAccessTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ExpiredAccessTokenException(String msg) {
        super(msg);
    }
}
