package com.mybooks.clientservice.exception;

public class FailedAuthenticationException extends RuntimeException {
    public FailedAuthenticationException(String message) {
        super(message);
    }
}
