package com.example.testlab.books;

import java.math.BigDecimal;

public record Book(Long id, String title, String author, String isbn, BigDecimal price) {

    Book withId(Long newId) {
        return new Book(newId, title, author, isbn, price);
    }
}
