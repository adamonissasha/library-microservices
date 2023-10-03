package com.example.bookservice.dto;

import com.example.bookservice.model.Genre;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private String ISBN;
    private String name;
    private Genre genre;
    private String description;
    private String author;
}
