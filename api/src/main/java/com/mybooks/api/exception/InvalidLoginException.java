package com.mybooks.api.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super("Username or password is incorrect");
    }
}
