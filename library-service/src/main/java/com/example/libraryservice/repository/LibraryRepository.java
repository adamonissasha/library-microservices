package com.example.libraryservice.repository;

import com.example.libraryservice.model.BookStatus;
import com.example.libraryservice.model.LibraryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<LibraryRecord, Long> {
    Optional<LibraryRecord> findByBookIdAndBookStatusAndEndDateGreaterThanEqual(Long bookId,
                                                                                BookStatus bookStatus,
                                                                                LocalDate endDate);

    Optional<LibraryRecord> findByBookIdAndBookStatus(Long bookId, BookStatus bookStatus);

    List<LibraryRecord> findAllByBookStatusAndEndDateGreaterThanEqual(BookStatus bookStatus,
                                                                      LocalDate endDate);
}
