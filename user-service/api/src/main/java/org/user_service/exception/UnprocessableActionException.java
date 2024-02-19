package org.user_service.exception;

public class UnprocessableActionException extends RuntimeException{
    public UnprocessableActionException(String message) {
        super(message);
    }
}
