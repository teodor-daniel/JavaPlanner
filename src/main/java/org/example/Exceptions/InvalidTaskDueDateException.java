package org.example.Exceptions;

public class InvalidTaskDueDateException extends RuntimeException {
    public InvalidTaskDueDateException(String message) {
        super(message);
    }
}