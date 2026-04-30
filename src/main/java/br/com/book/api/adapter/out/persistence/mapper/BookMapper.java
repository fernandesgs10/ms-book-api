package br.com.book.api.adapter.out.persistence.mapper;


import br.com.book.api.adapter.out.persistence.entity.AuthorEntity;
import br.com.book.api.adapter.out.persistence.entity.BookEntity;
import br.com.book.api.adapter.out.persistence.entity.CategoryEntity;
import br.com.book.api.adapter.out.persistence.entity.PublisherEntity;
import br.com.book.api.domain.Author;
import br.com.book.api.domain.Book;
import br.com.book.api.domain.Category;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookMapper implements Serializable {

    @Inject
    ModelMapper modelMapper;

    // Domain → Entitya

    public BookEntity toEntity(Book book) {
        if (book == null) return null;

        BookEntity entity = new BookEntity();

        entity.setId(book.getId());
        entity.setTitle(book.getTitle());
        entity.setSubtitle(book.getSubtitle());
        entity.setIsbn(book.getIsbn());
        entity.setPublicationYear(book.getPublicationYear());
        entity.setNumberOfPages(book.getNumberOfPages());
        entity.setPrice(book.getPrice());
        entity.setEdition(book.getEdition());
        entity.setSynopsis(book.getSynopsis());
        entity.setStockQuantity(book.getStockQuantity());

        // PUBLISHER
        if (book.getPublisher() != null && book.getPublisher().getId() != null) {
            PublisherEntity p = new PublisherEntity();
            p.setId(book.getPublisher().getId());
            entity.setPublisher(p);
        }

        // AUTHORS - LIMPEZA TOTAL
        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            List<AuthorEntity> authorEntities = new ArrayList<>();
            for (Author author : book.getAuthors()) {
                if (author != null && author.getId() != null && author.getId() > 0) {
                    AuthorEntity ae = new AuthorEntity();
                    ae.setId(author.getId());
                    authorEntities.add(ae);
                }
            }
            entity.setAuthors(authorEntities);
        } else {
            entity.setAuthors(new ArrayList<>());
        }

        // CATEGORIES
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            List<CategoryEntity> categoryEntities = new ArrayList<>();
            for (Category category : book.getCategories()) {
                if (category != null && category.getId() != null && category.getId() > 0) {
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(category.getId());
                    categoryEntities.add(ce);
                }
            }
            entity.setCategories(categoryEntities);
        } else {
            entity.setCategories(new ArrayList<>());
        }

        return entity;
    }

    // Entity → Domain
    public Book toDomain(BookEntity entity) {
        if (entity == null) return null;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(entity, Book.class);
    }

    // Lista Domain → Lista Entity
    public List<BookEntity> toEntityList(List<Book> books) {
        if (books == null) return List.of();
        return books.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // Lista Entity → Lista Domain
    public List<Book> toDomainList(List<BookEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}