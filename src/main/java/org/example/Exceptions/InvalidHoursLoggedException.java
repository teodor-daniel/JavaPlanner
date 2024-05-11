package org.example.Exceptions;

public class InvalidHoursLoggedException extends RuntimeException {
    public InvalidHoursLoggedException(String message) {
        super(message);
    }
}
