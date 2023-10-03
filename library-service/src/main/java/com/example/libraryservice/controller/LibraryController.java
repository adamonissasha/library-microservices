package com.example.libraryservice.controller;

import com.example.libraryservice.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/take-book/{id}")
    public ResponseEntity<Void> takeBook(@PathVariable("id") long id) {
        libraryService.takeBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/return-book/{id}")
    public ResponseEntity<Void> returnBook(@PathVariable("id") long id) {
        libraryService.returnBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
