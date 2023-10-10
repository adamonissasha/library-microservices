package com.example.bookservice.service.impl;

import com.example.bookservice.dto.request.BookRequest;
import com.example.bookservice.dto.response.BookResponse;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String BOOK_NOT_FOUND = "Book not found!";
    @Value("${library-service.url}")
    private String libraryServiceUrl;
    private static final String ADD_LIBRARY_RECORD_URL = "/{id}";
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    @Override
    public BookResponse addBook(BookRequest bookRequest, String token) {
        Book createdBook = mapBookRequestToBook(bookRequest);
        createdBook = bookRepository.save(createdBook);
        addLibraryRecord(createdBook.getId(), token);
        return mapBookToBookResponse(createdBook);
    }

    private void addLibraryRecord(long id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        restTemplate.exchange(
                libraryServiceUrl + ADD_LIBRARY_RECORD_URL,
                HttpMethod.POST,
                requestEntity,
                Void.class,
                id
        );
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapBookToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse getBookById(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));
        return mapBookToBookResponse(book);
    }

    @Override
    public BookResponse getBookByISBN(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));
        return mapBookToBookResponse(book);
    }

    @Override
    public BookResponse editBook(long id, BookRequest bookRequest) {
        Book updatedBook = mapBookRequestToBook(bookRequest);
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));
        updatedBook.setId(existingBook.getId());
        updatedBook = bookRepository.save(updatedBook);
        return mapBookToBookResponse(updatedBook);
    }

    @Override
    public void deleteBook(long id) {
        bookRepository.findById(id)
                .ifPresentOrElse(
                        book -> bookRepository.deleteById(id),
                        () -> {
                            throw new BookNotFoundException(BOOK_NOT_FOUND);
                        }
                );
    }

    public Book mapBookRequestToBook(BookRequest bookRequest) {
        return modelMapper.map(bookRequest, Book.class);
    }

    public BookResponse mapBookToBookResponse(Book book) {
        return modelMapper.map(book, BookResponse.class);
    }
}