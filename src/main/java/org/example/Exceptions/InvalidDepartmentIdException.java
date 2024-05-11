package org.example.Exceptions;

public class InvalidDepartmentIdException extends RuntimeException {
    public InvalidDepartmentIdException(String message) {
        super(message);
    }
}