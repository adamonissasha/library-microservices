package com.example.libraryservice.service.impl;

import com.example.libraryservice.dto.response.BookResponse;
import com.example.libraryservice.exception.BookNotFreeException;
import com.example.libraryservice.exception.BookFreeException;
import com.example.libraryservice.model.BookStatus;
import com.example.libraryservice.model.LibraryRecord;
import com.example.libraryservice.repository.LibraryRepository;
import com.example.libraryservice.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final RestTemplate restTemplate;
    private final LibraryRepository libraryRepository;
    private static final String BOOK_NOT_FREE_MESSAGE = "The book is not free!";
    private static final String BOOK_IS_FREE_MESSAGE = "The book is free!";
    @Value("${book-service.url}")
    private String bookServiceUrl;
    private static final String GET_BOOK_BY_ID_PATH = "/{id}";

    @Override
    public BookResponse takeBook(long bookId, int days, String token) {
        Optional<LibraryRecord> optionalLibraryRecord =
                libraryRepository.findByBookIdAndBookStatus(bookId, BookStatus.RETURNED);
        if (optionalLibraryRecord.isPresent()) {
            ResponseEntity<BookResponse> response = getBook(bookId, token);
            libraryRepository.save(LibraryRecord.builder()
                    .id(optionalLibraryRecord.get().getId())
                    .bookId(bookId)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(days))
                    .bookStatus(BookStatus.AWAITING_RETURN)
                    .build());
            return response.getBody();
        } else {
            throw new BookNotFreeException(BOOK_NOT_FREE_MESSAGE);
        }
    }

    public ResponseEntity<BookResponse> getBook(long bookId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(
                bookServiceUrl + GET_BOOK_BY_ID_PATH,
                HttpMethod.GET,
                requestEntity,
                BookResponse.class,
                bookId
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
                    throw new BookFreeException(BOOK_IS_FREE_MESSAGE);
                }
        );
    }

    @Override
    public List<BookResponse> getFreeBooks(String token) {
        List<Long> occupiedBookIds =
                libraryRepository.findAllByBookStatusAndEndDateGreaterThanEqual(BookStatus.AWAITING_RETURN, LocalDate.now())
                        .stream()
                        .map(LibraryRecord::getBookId)
                        .toList();
        return Objects.requireNonNull(getAllBooks(token).getBody())
                .stream()
                .filter(bookResponse -> !occupiedBookIds.contains(bookResponse.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void addNewRecord(long id) {
        libraryRepository.save(LibraryRecord.builder()
                .bookId(id)
                .bookStatus(BookStatus.RETURNED)
                .build());
    }

    public ResponseEntity<List<BookResponse>> getAllBooks(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(
                bookServiceUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );
    }
}
