package org.example.Exceptions;

public class InvalidBudgetException extends RuntimeException {
    public InvalidBudgetException(String message) {
        super(message);
    }
}