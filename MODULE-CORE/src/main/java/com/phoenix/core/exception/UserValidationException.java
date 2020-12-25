package com.phoenix.core.exception;

public class UserValidationException extends RuntimeException {
    public UserValidationException(final String message) {
        super(message);
    }
}
