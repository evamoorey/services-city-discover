package org.city_discover.exception;

public class TooMuchRequestsException extends RuntimeException {
    public TooMuchRequestsException(String message) {
        super(message);
    }
}
