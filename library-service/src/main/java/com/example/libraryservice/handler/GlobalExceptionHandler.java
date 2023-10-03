package com.example.libraryservice.handler;

import com.example.libraryservice.exception.BookBusyException;
import com.example.libraryservice.exception.BookFreeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BookBusyException.class)
    public ResponseEntity<String> handleBookException(BookBusyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = BookFreeException.class)
    public ResponseEntity<String> handleBookException(BookFreeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
