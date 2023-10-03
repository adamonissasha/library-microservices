package com.example.bookservice.handler;

import com.example.bookservice.exception.BookBusyException;
import com.example.bookservice.exception.BookFreeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class MyResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            throw new BookBusyException("This book is busy!");
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new BookFreeException("The book is free!");
        } else {
            throw new RuntimeException(response.getStatusCode() + response.getStatusText());
        }
    }
}
