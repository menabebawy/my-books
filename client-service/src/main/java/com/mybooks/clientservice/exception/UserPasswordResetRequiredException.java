package com.mybooks.clientservice.exception;

public class UserPasswordResetRequiredException extends RuntimeException {
    public UserPasswordResetRequiredException(String username) {
        super(username + " password reset required");
    }
}
