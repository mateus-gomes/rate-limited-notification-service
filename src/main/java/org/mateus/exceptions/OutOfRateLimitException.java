package org.mateus.exceptions;

public class OutOfRateLimitException extends RuntimeException {

    private String exception;

    public OutOfRateLimitException(String message) {
        super(message);
        this.exception = message;
    }
}
