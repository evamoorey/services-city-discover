package org.user_service.exception;

public class TooMuchRequestsException extends RuntimeException {
    public TooMuchRequestsException(String message) {
        super(message);
    }
}
