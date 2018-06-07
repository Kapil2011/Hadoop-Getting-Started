package com.kapildas.afpvalidator;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable th) {
        super(message, th);
    }
}
