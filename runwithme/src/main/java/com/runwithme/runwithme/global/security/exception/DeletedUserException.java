package com.runwithme.runwithme.global.security.exception;

public class DeletedUserException extends RuntimeException{
    public DeletedUserException() {
        super();
    }

    public DeletedUserException(String message) {
        super(message);
    }
}
