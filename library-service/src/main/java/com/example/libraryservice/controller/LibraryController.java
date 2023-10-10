package com.example.libraryservice.controller;

import com.example.libraryservice.dto.response.BookResponse;
import com.example.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/take/{id}")
    @Operation(summary = "Take book by ID", description = "Take a book by its ID from the library.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully taken the book."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "400", description = "Book is busy."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @ResponseStatus(HttpStatus.OK)
    public BookResponse takeBookById(@PathVariable("id")
                                     @Parameter(description = "Unique ID of the book to be taken.",
                                             example = "1", required = true) long id,
                                     @RequestHeader(HttpHeaders.AUTHORIZATION)
                                     @Parameter(description = "Bearer token for authentication.",
                                             required = true) String token,
                                     @RequestParam("days") int days) {
        return libraryService.takeBook(id, days, token);
    }

    @PostMapping("/return/{id}")
    @Operation(summary = "Return book by ID", description = "Return a book to the library.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully returned the book."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "404", description = "Book is free."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @ResponseStatus(HttpStatus.OK)
    public void returnBook(@PathVariable("id")
                           @Parameter(description = "Unique ID of the book to be returned.",
                                   example = "1", required = true) long id) {
        libraryService.returnBook(id);
    }

    @Operation(summary = "Get free books ID", description = "Get all free books id in the library.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully got free books."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid token."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @GetMapping("/free")
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> freeBooks(@RequestHeader(HttpHeaders.AUTHORIZATION)
                                        @Parameter(description = "Bearer token for authentication.",
                                                required = true) String token) {
        return libraryService.getFreeBooks(token);
    }

    @PostMapping("/{id}")
    public void addRecord(@PathVariable("id")
                          @Parameter(description = "Unique ID of the book to be added.",
                                  example = "1", required = true) long id) {
        libraryService.addNewRecord(id);
    }
}
