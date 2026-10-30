package com.example.testlab.books;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public List<Book> findAll() {
        return books.values().stream().sorted(Comparator.comparing(Book::id)).toList();
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(books.get(id));
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return books.values().stream().filter(book -> book.isbn().equals(isbn)).findFirst();
    }

    @Override
    public Book save(Book book) {
        Book saved = book.id() == null ? book.withId(nextId.getAndIncrement()) : book;
        books.put(saved.id(), saved);
        return saved;
    }

    @Override
    public boolean deleteById(long id) {
        return books.remove(id) != null;
    }
}
