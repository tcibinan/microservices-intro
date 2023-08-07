package org.tcibinan.exception;

public class ValidationError extends RuntimeException {

    public ValidationError() {
    }

    public ValidationError(String message) {
        super(message);
    }
}
