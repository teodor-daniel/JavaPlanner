package org.example.Exceptions;

public class InvalidEmploymentStatusException extends IllegalArgumentException {
    public InvalidEmploymentStatusException(String message){
        super(message);
    }
}
