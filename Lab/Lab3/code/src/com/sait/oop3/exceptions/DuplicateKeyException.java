package com.sait.oop3.exceptions;

/**
 * Thrown when attempting to insert a key that already exists in the dictionary.
 */
public class DuplicateKeyException extends Exception {
    public DuplicateKeyException() {
        super();
    }

    public DuplicateKeyException(String message) {
        super(message);
    }

    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
