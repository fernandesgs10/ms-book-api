package br.com.book.api.application.port.out;

import br.com.book.api.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryPort {

    List<Book> findAllBooks();

    Optional<Book> findBookById(Long id);           // ← renomeado
    Optional<Book> findBookByIsbn(String isbn);     // ← renomeado

    List<Book> findByTitle(String title);
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByCategoryId(Long categoryId);
    List<Book> findByPublisherId(Long publisherId);
    List<Book> findAvailableBooks();

    Book save(Book book);

    void deleteBookById(Long id);                   // ← renomeado

    boolean existsBookById(Long id);                // ← renomeado
    boolean existsBookByIsbn(String isbn);          // ← renomeado
}