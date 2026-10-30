package com.example.testlab.books;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record BookRequest(
        @NotBlank @Size(max = 255) String title,
        @NotBlank String author,
        @NotNull @Pattern(regexp = "\\d{10}|\\d{13}", message = "must be 10 or 13 digits") String isbn,
        @NotNull @DecimalMin("0") BigDecimal price) {

    Book toBook(Long id) {
        return new Book(id, title, author, isbn, price);
    }
}
