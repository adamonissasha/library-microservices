package com.example.bookservice.controller;

import com.example.bookservice.dto.request.BookRequest;
import com.example.bookservice.dto.response.BookResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping()
    @Operation(summary = "Create a new book",
            description = "Creates a new book with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully created."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse addBook(@RequestBody
                                @Parameter(description = "The book data in JSON format.", required = true)
                                BookRequest createBookRequest,
                                @RequestHeader(HttpHeaders.AUTHORIZATION)
                                @Parameter(description = "Bearer token for authentication.",
                                        required = true) String token) {
        return bookService.addBook(createBookRequest, token);
    }

    @GetMapping()
    @Operation(summary = "Get all books", description = "Retrieves a list of all books available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of books retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves information about a book by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book information retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @ResponseStatus(HttpStatus.OK)
    public BookResponse getBookById(@PathVariable("id")
                                    @Parameter(description = "Unique ID of the book.",
                                            example = "1", required = true) long id) {

        return bookService.getBookById(id);
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get book by ISBN", description = "Retrieves information about a book by its ISBN number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book information retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @ResponseStatus(HttpStatus.OK)
    public BookResponse getBookByISBN(@PathVariable("isbn")
                                      @Parameter(description = "ISBN of the book.",
                                              example = "1", required = true) String isbn) {
        return bookService.getBookByISBN(isbn);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by ID", description = "Deletes a book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteBookById(@PathVariable("id")
                               @Parameter(description = "Unique ID of the book to be deleted.",
                                       example = "1", required = true) Long id) {
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit book by ID", description = "Updates information about a book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request data or ID not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @ResponseStatus(HttpStatus.OK)
    public BookResponse editBookById(@PathVariable("id")
                                     @Parameter(description = "Unique ID of the book to be updated.",
                                             example = "1", required = true) Long id,
                                     @RequestBody
                                     @Parameter(description = "Updated book data.",
                                             required = true) BookRequest bookRequest) {
        return bookService.editBook(id, bookRequest);
    }
}
