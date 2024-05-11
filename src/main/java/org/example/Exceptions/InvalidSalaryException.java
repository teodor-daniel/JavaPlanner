package org.example.Exceptions;

public class InvalidSalaryException extends IllegalArgumentException{
   public InvalidSalaryException(String message){
       super(message);
   }
}
