package com.example.bookservice.service.impl;

import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String BOOK_NOT_FOUND = "Book not found!";
    private static final String LIBRARY_SERVICE_URL = "http://localhost:8081/library/";

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;


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

    @Override
    public ResponseEntity<Void> takeBook(long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        if (optionalBook.isPresent()) {
            return restTemplate.exchange(
                    LIBRARY_SERVICE_URL + "take-book/{variable}",
                    HttpMethod.POST,
                    requestEntity,
                    Void.class,
                    bookId
            );
        } else {
            throw new BookNotFoundException(BOOK_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Void> returnBook(long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        if (optionalBook.isPresent()) {
            return restTemplate.exchange(
                    LIBRARY_SERVICE_URL + "return-book/{variable}",
                    HttpMethod.POST,
                    requestEntity,
                    Void.class,
                    id
            );
        } else {
            throw new BookNotFoundException(BOOK_NOT_FOUND);
        }
    }
}