package org.example.Exceptions;

public class InvalidDepartmentException extends RuntimeException {
    public InvalidDepartmentException(String message) {
        super(message);
    }
}
