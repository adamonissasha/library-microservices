package com.example.libraryservice.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private long id;
    private String isbn;
    private String name;
    private String genre;
    private String description;
    private String author;
}
