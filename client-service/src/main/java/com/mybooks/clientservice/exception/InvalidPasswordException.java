package com.mybooks.clientservice.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Invalid password");
    }
}
