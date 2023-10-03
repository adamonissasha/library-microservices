package com.example.libraryservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "library_record")
public class LibraryRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long bookId;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;
}
