package com.example.bookservice.service.impl;

import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String BOOK_NOT_FOUND = "Book not found!";
    private final BookRepository bookRepository;

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));
    }

    @Override
    public Book getBookByISBN(String isbn) {
        return bookRepository.findByISBN(isbn).orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));
    }

    @Override
    public void editBook(long id, Book updatedBook) {
        bookRepository.findById(id).ifPresentOrElse(
                book -> bookRepository.save(Book.builder()
                        .id(id)
                        .name(updatedBook.getName())
                        .author(updatedBook.getAuthor())
                        .ISBN(updatedBook.getISBN())
                        .genre(updatedBook.getGenre())
                        .description(updatedBook.getDescription())
                        .build()),
                () -> {
                    throw new BookNotFoundException(BOOK_NOT_FOUND);
                }
        );
    }

    @Override
    public void deleteBook(long id) {
        bookRepository.findById(id).ifPresentOrElse(
                book -> bookRepository.deleteById(id),
                () -> {
                    throw new BookNotFoundException(BOOK_NOT_FOUND);
                }
        );
    }
}