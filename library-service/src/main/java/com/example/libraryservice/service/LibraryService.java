package com.example.libraryservice.service;

import java.util.List;

public interface LibraryService {
    void takeBook(long bookId);

    void returnBook(long id);

    List<Long> getFreeBooks();
}
