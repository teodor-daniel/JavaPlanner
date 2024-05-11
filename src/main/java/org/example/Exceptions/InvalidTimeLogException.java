package org.example.Exceptions;

public class InvalidTimeLogException extends RuntimeException {
    public InvalidTimeLogException(String message) {
        super(message);
    }
}