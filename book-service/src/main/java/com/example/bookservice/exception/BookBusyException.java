package com.example.bookservice.exception;

public class BookBusyException extends RuntimeException {

    public BookBusyException(String message) {
        super(message);
    }
}
