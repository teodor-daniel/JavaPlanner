package org.example.Exceptions;

public class InvalidTaskIdException extends RuntimeException {
    public InvalidTaskIdException(String message) {
        super(message);
    }
}