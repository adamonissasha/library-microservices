package com.example.bookservice.service.impl;

import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String BOOK_NOT_FOUND = "Book not found!";
    @Value("${library-service.url}")
    private String libraryServiceUrl;

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
    public ResponseEntity<Void> takeBook(long bookId, String token) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        if (optionalBook.isPresent()) {
            return restTemplate.exchange(
                    libraryServiceUrl + "take-book/{variable}",
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
    public ResponseEntity<Void> returnBook(long id, String token) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        if (optionalBook.isPresent()) {
            return restTemplate.exchange(
                    libraryServiceUrl + "return-book/{variable}",
                    HttpMethod.POST,
                    requestEntity,
                    Void.class,
                    id
            );
        } else {
            throw new BookNotFoundException(BOOK_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> getFreeBooks(String token) {
        List<Book> books = bookRepository.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<ArrayList<Long>> response = restTemplate.exchange(
                libraryServiceUrl + "free-books",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            List<Long> ids = response.getBody();
            List<Book> freeBooks = books.stream()
                    .filter(book -> {
                        assert ids != null;
                        return !ids.contains(book.getId());
                    }).toList();
            return ResponseEntity.ok(freeBooks);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}