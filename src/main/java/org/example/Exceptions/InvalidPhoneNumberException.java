package org.example.Exceptions;

public class InvalidPhoneNumberException extends IllegalArgumentException {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
