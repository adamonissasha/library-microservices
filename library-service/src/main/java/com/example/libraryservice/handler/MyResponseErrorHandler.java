package com.example.libraryservice.handler;

import com.example.libraryservice.exception.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class MyResponseErrorHandler implements ResponseErrorHandler {
    private static final String BOOK_NOT_FOUND_MESSAGE = "Book not found!";
    private static final String ERROR = "Error ";

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new BookNotFoundException(BOOK_NOT_FOUND_MESSAGE);
        } else {
            throw new RuntimeException(ERROR + response.getStatusCode() + response.getStatusText());
        }
    }
}
