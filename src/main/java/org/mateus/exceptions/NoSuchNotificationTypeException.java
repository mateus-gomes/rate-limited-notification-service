package org.mateus.exceptions;

public class NoSuchNotificationTypeException extends RuntimeException {

    private String exception;

    public NoSuchNotificationTypeException(String message) {
        super(message);
        this.exception = message;
    }
}
