package org.example.Exceptions;

public class InvalidTaskAssignmentException extends RuntimeException {
    public InvalidTaskAssignmentException(String message) {
        super(message);
    }
}