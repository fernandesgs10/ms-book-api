package br.com.book.api.adapter.out.persistence.repository;

import br.com.book.api.adapter.out.persistence.entity.AuthorEntity;
import br.com.book.api.adapter.out.persistence.entity.CategoryEntity;
import br.com.book.api.adapter.out.persistence.entity.PublisherEntity;
import br.com.book.api.adapter.out.persistence.mapper.BookMapper;
import br.com.book.api.application.port.out.BookRepositoryPort;

import br.com.book.api.adapter.out.persistence.entity.BookEntity;

import br.com.book.api.domain.Author;
import br.com.book.api.domain.Book;
import br.com.book.api.domain.Category;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class BookRepositoryAdapter implements BookRepositoryPort, PanacheRepository<BookEntity> {

    @Inject
    BookMapper bookMapper;

    // ========== Métodos da interface BookRepositoryPort ==========

    @Override
    public List<Book> findAllBooks() {
        return bookMapper.toDomainList(listAll());
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        return findByIdOptional(id).map(bookMapper::toDomain);
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        return find("isbn", isbn).firstResultOptional().map(bookMapper::toDomain);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return bookMapper.toDomainList(
                find("title LIKE :title", Parameters.with("title", "%" + title + "%")).list()
        );
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) {
        return bookMapper.toDomainList(
                find("SELECT b FROM BookEntity b JOIN b.authors a WHERE a.id = :authorId",
                        Parameters.with("authorId", authorId)).list()
        );
    }

    @Override
    public List<Book> findByCategoryId(Long categoryId) {
        return bookMapper.toDomainList(
                find("SELECT b FROM BookEntity b JOIN b.categories c WHERE c.id = :categoryId",
                        Parameters.with("categoryId", categoryId)).list()
        );
    }

    @Override
    public List<Book> findByPublisherId(Long publisherId) {
        return bookMapper.toDomainList(
                find("publisher.id = :publisherId", Parameters.with("publisherId", publisherId)).list()
        );
    }

    @Override
    public List<Book> findAvailableBooks() {
        return bookMapper.toDomainList(find("stockQuantity > 0").list());
    }

    @Override
    public Book save(Book book) {
        // Limpeza dos autores
        if (book.getAuthors() != null) {
            List<Author> validAuthors = book.getAuthors().stream()
                    .filter(a -> a != null && a.getId() != null)
                    .collect(Collectors.toList());
            book.setAuthors(validAuthors);
            log.info("Autores após limpeza: {}", validAuthors.stream().map(Author::getId).collect(Collectors.toList()));
        }

        BookEntity entity = bookMapper.toEntity(book);

        // VALIDAÇÃO FINAL
        if (entity.getAuthors() != null) {
            entity.setAuthors(entity.getAuthors().stream()
                    .filter(a -> a != null && a.getId() != null)
                    .collect(Collectors.toList()));
        }

        if (entity.getCategories() != null) {
            entity.setCategories(entity.getCategories().stream()
                    .filter(c -> c != null && c.getId() != null)
                    .collect(Collectors.toList()));
        }

        BookEntity managed;

        // 🔥 USAR persist PARA CRIAÇÃO, merge PARA ATUALIZAÇÃO
        if (entity.getId() == null) {
            getEntityManager().persist(entity);
            managed = entity;
        } else {
            managed = getEntityManager().merge(entity);
        }

        return bookMapper.toDomain(managed);
    }


    @Override
    public void deleteBookById(Long id) {
        deleteById(id);
    }

    @Override
    public boolean existsBookById(Long id) {
        return findByIdOptional(id).isPresent();
    }

    @Override
    public boolean existsBookByIsbn(String isbn) {
        return count("isbn", isbn) > 0;
    }
}