package com.example.bookservice.dto.request;

import com.example.bookservice.model.Genre;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String isbn;
    private String name;
    private Genre genre;
    private String description;
    private String author;
}
