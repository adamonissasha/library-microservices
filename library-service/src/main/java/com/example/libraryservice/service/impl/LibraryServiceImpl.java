package com.example.libraryservice.service.impl;

import com.example.libraryservice.exception.BookBusyException;
import com.example.libraryservice.exception.BookFreeException;
import com.example.libraryservice.model.BookStatus;
import com.example.libraryservice.model.LibraryRecord;
import com.example.libraryservice.repository.LibraryRepository;
import com.example.libraryservice.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final LibraryRepository libraryRepository;

    @Override
    public void takeBook(long bookId) {
        LocalDate today = LocalDate.now();
        libraryRepository.findByBookIdAndBookStatusAndEndDateGreaterThanEqual(bookId, BookStatus.AWAITING_RETURN, today).ifPresentOrElse(
                library -> {
                    throw new BookBusyException("The book is busy!");
                },
                () -> libraryRepository.save(LibraryRecord.builder()
                        .bookId(bookId)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(30))
                        .bookStatus(BookStatus.AWAITING_RETURN)
                        .build())
        );
    }

    @Override
    public void returnBook(long id) {
        libraryRepository.findByBookIdAndBookStatus(id, BookStatus.AWAITING_RETURN).ifPresentOrElse(
                libraryRecord -> {
                    libraryRecord.setBookStatus(BookStatus.RETURNED);
                    libraryRecord.setEndDate(LocalDate.now());
                    libraryRepository.save(libraryRecord);
                },
                () -> {
                    throw new BookFreeException("The book is free!");
                }
        );
    }
}
