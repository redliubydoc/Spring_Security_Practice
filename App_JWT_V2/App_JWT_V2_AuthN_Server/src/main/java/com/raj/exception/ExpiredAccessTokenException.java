package com.raj.exception;

public class ExpiredAccessTokenException extends Exception {
    public ExpiredAccessTokenException() {
    }

    public ExpiredAccessTokenException(String message) {
        super(message);
    }

    public ExpiredAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredAccessTokenException(Throwable cause) {
        super(cause);
    }

    protected ExpiredAccessTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
