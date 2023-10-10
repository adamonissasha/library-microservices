package com.example.bookservice.dto.response;

import com.example.bookservice.model.Genre;
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
    private Genre genre;
    private String description;
    private String author;
}
