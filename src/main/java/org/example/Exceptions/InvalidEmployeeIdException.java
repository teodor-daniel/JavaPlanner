package org.example.Exceptions;

public class InvalidEmployeeIdException extends RuntimeException {
    public InvalidEmployeeIdException(String message) {
        super(message);
    }
}