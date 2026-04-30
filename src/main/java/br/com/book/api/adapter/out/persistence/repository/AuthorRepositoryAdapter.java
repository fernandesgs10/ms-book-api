package br.com.book.api.adapter.out.persistence.repository;

import br.com.book.api.adapter.out.persistence.mapper.AuthorMapper;
import br.com.book.api.application.port.out.AuthorRepositoryPort;

import br.com.book.api.adapter.out.persistence.entity.AuthorEntity;

import br.com.book.api.domain.Author;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AuthorRepositoryAdapter implements AuthorRepositoryPort, PanacheRepository<AuthorEntity> {

    @Inject
    AuthorMapper authorMapper;

    @Override
    public List<Author> findAllAuthors() {
        return authorMapper.toDomainList(listAll());
    }

    @Override
    public Optional<Author> findAuthorById(Long id) {
        return findByIdOptional(id).map(authorMapper::toDomain);
    }

    @Override
    public List<Author> findAuthorsByName(String name) {
        return authorMapper.toDomainList(
                find("name LIKE :name", Parameters.with("name", "%" + name + "%")).list()
        );
    }

    @Override
    public List<Author> findAuthorsByNationality(String nationality) {
        return authorMapper.toDomainList(
                find("nationality = :nationality", Parameters.with("nationality", nationality)).list()
        );
    }

    @Override
    public Optional<Author> findAuthorByIdWithBooks(Long id) {
        // Este método carrega o autor com seus livros (fetch)
        return find("SELECT DISTINCT a FROM AuthorEntity a LEFT JOIN FETCH a.books WHERE a.id = :id",
                Parameters.with("id", id))
                .firstResultOptional()
                .map(authorMapper::toDomain);
    }

    @Override
    public List<Author> findAuthorsWithBooksPublishedInYear(Integer year) {
        return authorMapper.toDomainList(
                find("SELECT DISTINCT a FROM AuthorEntity a JOIN a.books b WHERE b.publicationYear = :year",
                        Parameters.with("year", year)).list()
        );
    }

    @Override
    public Author save(Author author) {
        AuthorEntity entity = authorMapper.toEntity(author);
        persist(entity);
        return authorMapper.toDomain(entity);
    }

    @Override
    public void deleteAuthorById(Long id) {
        deleteById(id);
    }

    @Override
    public boolean existsAuthorById(Long id) {
        return findByIdOptional(id).isPresent();
    }

    @Override
    public long countAuthorsByNationality(String nationality) {
        return count("nationality = :nationality", Parameters.with("nationality", nationality));
    }
}