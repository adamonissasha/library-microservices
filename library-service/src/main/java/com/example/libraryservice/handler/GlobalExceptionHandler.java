package com.example.libraryservice.handler;

import com.example.libraryservice.exception.BookNotFoundException;
import com.example.libraryservice.exception.BookNotFreeException;
import com.example.libraryservice.exception.BookFreeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BookNotFreeException.class)
    public ResponseEntity<String> handleBookException(BookNotFreeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = BookFreeException.class)
    public ResponseEntity<String> handleBookException(BookFreeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookException(BookNotFoundException ex) {
        return ex.getMessage();
    }

}
