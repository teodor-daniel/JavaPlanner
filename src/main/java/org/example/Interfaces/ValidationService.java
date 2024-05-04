package org.example.Interfaces;

public interface ValidationService<T> {

    boolean validate(T entity);
}