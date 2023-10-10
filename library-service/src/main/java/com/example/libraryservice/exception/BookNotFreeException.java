package com.example.libraryservice.exception;

public class BookNotFreeException extends RuntimeException {

    public BookNotFreeException(String message) {
        super(message);
    }
}
