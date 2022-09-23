package com.mybooks.oauthservice.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String email) {
        super(String.format("User with email %s is already existed", email));
    }
}
