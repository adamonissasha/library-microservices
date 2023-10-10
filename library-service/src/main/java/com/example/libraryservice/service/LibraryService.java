package com.example.libraryservice.service;

import com.example.libraryservice.dto.response.BookResponse;

import java.util.List;

public interface LibraryService {
    BookResponse takeBook(long bookId, int days, String token);

    void returnBook(long id);

    List<BookResponse> getFreeBooks(String token);

    void addNewRecord(long id);
}
