package com.example.bookservice.controller;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.model.Book;
import com.example.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create a new book",
            description = "Creates a new book with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully created."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<Void> addBook(@RequestBody
                                        @Parameter(description = "The book data in JSON format.", required = true)
                                        BookDTO bookDTO) {
        Book book = this.modelMapper.map(bookDTO, Book.class);
        bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    @Operation(summary = "Get all books", description = "Retrieves a list of all books available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of books retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves information about a book by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book information retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<?> getBookById(@PathVariable("id")
                                         @Parameter(description = "Unique ID of the book.",
                                                 example = "1", required = true) long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get book by ISBN", description = "Retrieves information about a book by its ISBN number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book information retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<?> getBookByISBN(@PathVariable("isbn")
                                           @Parameter(description = "ISBN of the book.",
                                                   example = "1", required = true) String isbn) {
        Book book = bookService.getBookByISBN(isbn);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by ID", description = "Deletes a book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<String> deleteBookById(@PathVariable("id")
                                                 @Parameter(description = "Unique ID of the book to be deleted.",
                                                         example = "1", required = true) Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit book by ID", description = "Updates information about a book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request data or ID not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<String> editBookById(@PathVariable("id")
                                               @Parameter(description = "Unique ID of the book to be updated.",
                                                       example = "1", required = true) Long id,
                                               @RequestBody
                                               @Parameter(description = "Updated book data.",
                                                       required = true) BookDTO updatedBookDTO) {
        Book updatedBook = this.modelMapper.map(updatedBookDTO, Book.class);
        bookService.editBook(id, updatedBook);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/take-book/{id}")
    @Operation(summary = "Take book by ID", description = "Take a book by its ID from the library.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully taken the book."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<?> takeBook(@PathVariable("id")
                                      @Parameter(description = "Unique ID of the book to be taken.",
                                              example = "1", required = true) long id,
                                      @RequestHeader("Authorization")
                                      @Parameter(description = "Bearer token for authentication.",
                                              required = true) String token) {
        return bookService.takeBook(id, token);
    }

    @PostMapping("/return-book/{id}")
    @Operation(summary = "Return book by ID", description = "Return a book to the library.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully returned the book."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<?> returnBook(@PathVariable("id")
                                        @Parameter(description = "Unique ID of the book to be returned.",
                                                example = "1", required = true) long id,
                                        @RequestHeader("Authorization")
                                        @Parameter(description = "Bearer token for authentication.",
                                                required = true) String token) {
        return bookService.returnBook(id, token);
    }

    @GetMapping("/get-free-books")
    @Operation(summary = "Get free books", description = "Get all free books in the library.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully returned the book."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<?> getFreeBooks(@RequestHeader("Authorization")
                                        @Parameter(description = "Bearer token for authentication.",
                                                required = true) String token) {
        return bookService.getFreeBooks(token);
    }
}
