package com.example.bookservice.controller;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.model.Book;
import com.example.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @PostMapping()
    public ResponseEntity<Void> addBook(@RequestBody BookDTO bookDTO) {
        Book book = this.modelMapper.map(bookDTO, Book.class);
        bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable("id") long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<?> getBookByISBN(@PathVariable("isbn") String isbn) {
        Book book = bookService.getBookByISBN(isbn);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editBookById(@PathVariable("id") Long id,
                                               @RequestBody BookDTO updatedBookDTO) {
        Book updatedBook = this.modelMapper.map(updatedBookDTO, Book.class);
        bookService.editBook(id, updatedBook);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/take-book/{id}")
    public ResponseEntity<?> takeBook(@PathVariable("id") long id) {
        return bookService.takeBook(id);
    }

    @PostMapping("/return-book/{id}")
    public ResponseEntity<?> returnBook(@PathVariable("id") long id) {
        return bookService.returnBook(id);
    }
}
