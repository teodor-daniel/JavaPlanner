package org.example.Exceptions;

public class InvalidAgeException extends IllegalArgumentException{
    public InvalidAgeException(String message){
        super(message);
    }
}
