package com.raj.exception;

public class RefreshTokenGenerationException extends Exception {
    public RefreshTokenGenerationException() {
        super();
    }

    public RefreshTokenGenerationException(String message) {
        super(message);
    }

    public RefreshTokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenGenerationException(Throwable cause) {
        super(cause);
    }

    protected RefreshTokenGenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
