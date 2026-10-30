package com.example.testlab.books;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Book findById(long id) {
        return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    // synchronized so the ISBN uniqueness check and the save happen atomically
    public synchronized Book create(BookRequest request) {
        if (repository.findByIsbn(request.isbn()).isPresent()) {
            throw new DuplicateIsbnException(request.isbn());
        }
        return repository.save(request.toBook(null));
    }

    public synchronized Book update(long id, BookRequest request) {
        findById(id);
        repository.findByIsbn(request.isbn())
                .filter(other -> other.id() != id)
                .ifPresent(other -> {
                    throw new DuplicateIsbnException(request.isbn());
                });
        return repository.save(request.toBook(id));
    }

    public void delete(long id) {
        if (!repository.deleteById(id)) {
            throw new BookNotFoundException(id);
        }
    }
}
