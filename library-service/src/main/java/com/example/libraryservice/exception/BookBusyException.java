package com.example.libraryservice.exception;

public class BookBusyException extends RuntimeException {

    public BookBusyException(String message) {
        super(message);
    }
}
