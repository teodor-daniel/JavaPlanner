package org.example.Exceptions;

public class InvalidProjectIdException extends RuntimeException {
    public InvalidProjectIdException(String message) {
        super(message);
    }
}