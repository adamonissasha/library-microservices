package com.example.bookservice.service;

import com.example.bookservice.model.Book;

import java.util.List;

public interface BookService {
    void addBook(Book book);

    List<Book> getAllBooks();

    Book getBookById(long id);

    Book getBookByISBN(String isbn);

    void editBook(long id, Book book);

    void deleteBook(long id);
}
