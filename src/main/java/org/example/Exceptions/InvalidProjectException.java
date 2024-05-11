package org.example.Exceptions;

public class InvalidProjectException extends RuntimeException {
    public InvalidProjectException(String message) {
        super(message);
    }
}