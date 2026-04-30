package br.com.book.api.application.port.out;


import br.com.book.api.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepositoryPort {

    // Buscas
    List<Author> findAllAuthors();

    Optional<Author> findAuthorById(Long id);

    List<Author> findAuthorsByName(String name);

    List<Author> findAuthorsByNationality(String nationality);

    Optional<Author> findAuthorByIdWithBooks(Long id);

    List<Author> findAuthorsWithBooksPublishedInYear(Integer year);

    // Operações
    Author save(Author author);

    void deleteAuthorById(Long id);

    // Validações
    boolean existsAuthorById(Long id);

    // Contagem
    long countAuthorsByNationality(String nationality);
}