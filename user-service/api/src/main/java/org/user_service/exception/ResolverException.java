package org.user_service.exception;

public class ResolverException extends RuntimeException {

    public ResolverException() {
        super("Unable to find suitable wrapper for your data");
    }
}

