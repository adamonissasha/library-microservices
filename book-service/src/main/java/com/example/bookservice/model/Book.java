package com.example.bookservice.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(unique = true)
    private String isbn;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column
    private String description;

    @Column
    private String author;
}