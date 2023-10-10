package com.example.bookservice.service;

import com.example.bookservice.dto.request.BookRequest;
import com.example.bookservice.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse addBook(BookRequest createBookRequest, String token);

    List<BookResponse> getAllBooks();

    BookResponse getBookById(long id);

    BookResponse getBookByISBN(String isbn);

    BookResponse editBook(long id, BookRequest bookRequest);

    void deleteBook(long id);
}
