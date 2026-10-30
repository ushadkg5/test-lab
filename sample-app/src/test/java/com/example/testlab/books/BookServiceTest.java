package com.example.testlab.books;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final BookRequest REQUEST =
            new BookRequest("Dune", "Frank Herbert", "9780441013593", new BigDecimal("9.99"));

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    @Test
    void createSavesBookWhenIsbnIsNew() {
        Book saved = REQUEST.toBook(1L);
        when(repository.findByIsbn(REQUEST.isbn())).thenReturn(Optional.empty());
        when(repository.save(REQUEST.toBook(null))).thenReturn(saved);

        assertThat(service.create(REQUEST)).isEqualTo(saved);
    }

    @Test
    void createRejectsIsbnThatAlreadyExists() {
        when(repository.findByIsbn(REQUEST.isbn())).thenReturn(Optional.of(REQUEST.toBook(7L)));

        assertThatThrownBy(() -> service.create(REQUEST)).isInstanceOf(DuplicateIsbnException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void findByIdThrowsNotFoundWhenBookIsMissing() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(42L)).isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void updateAllowsBookToKeepItsOwnIsbn() {
        Book existing = REQUEST.toBook(5L);
        when(repository.findById(5L)).thenReturn(Optional.of(existing));
        when(repository.findByIsbn(REQUEST.isbn())).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        assertThat(service.update(5L, REQUEST)).isEqualTo(existing);
    }

    @Test
    void updateRejectsIsbnOwnedByAnotherBook() {
        when(repository.findById(5L)).thenReturn(Optional.of(REQUEST.toBook(5L)));
        when(repository.findByIsbn(REQUEST.isbn())).thenReturn(Optional.of(REQUEST.toBook(6L)));

        assertThatThrownBy(() -> service.update(5L, REQUEST)).isInstanceOf(DuplicateIsbnException.class);
    }

    @Test
    void updateThrowsNotFoundWhenBookIsMissing() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(5L, REQUEST)).isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void deleteThrowsNotFoundWhenBookIsMissing() {
        when(repository.deleteById(5L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(5L)).isInstanceOf(BookNotFoundException.class);
    }
}
