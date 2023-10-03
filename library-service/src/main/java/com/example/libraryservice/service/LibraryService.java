package com.example.libraryservice.service;

public interface LibraryService {
    void takeBook(long bookId);

    void returnBook(long id);
}
