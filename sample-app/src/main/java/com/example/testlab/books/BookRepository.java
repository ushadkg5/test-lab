package com.example.testlab.books;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findById(long id);

    Optional<Book> findByIsbn(String isbn);

    /** Saves the book, assigning an id when it has none. */
    Book save(Book book);

    boolean deleteById(long id);
}
