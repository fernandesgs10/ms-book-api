package br.com.book.api.adapter.in.rest.mapper;

import br.com.book.api.domain.Book;
import br.com.book.api.gen.model.BookCreateRequest;
import br.com.book.api.gen.model.BookUpdateRequest;
import br.com.book.api.gen.model.BookResponse;
import br.com.book.api.application.port.in.book.CreateBookUseCase;
import br.com.book.api.application.port.in.book.UpdateBookUseCase;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookDTOMapper {

    @Inject
    ModelMapper modelMapper;

    @PostConstruct
    void configure() {

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true);

        // ✔️ Map simples de request → domain (SEM RELAÇÕES JPA DIRETAS)
        modelMapper.typeMap(BookCreateRequest.class, Book.class)
                .addMappings(mapper -> {
                    mapper.skip(Book::setAuthors);
                    mapper.skip(Book::setCategories);
                });

        modelMapper.typeMap(BookUpdateRequest.class, Book.class)
                .addMappings(mapper -> {
                    mapper.skip(Book::setAuthors);
                    mapper.skip(Book::setCategories);
                });
    }

    // =========================
    // RESPONSE
    // =========================
    public BookResponse toResponse(Book book) {
        return modelMapper.map(book, BookResponse.class);
    }

    // =========================
    // DOMAIN (SEM RELAÇÕES JPA)
    // =========================
    public Book toDomain(BookCreateRequest request) {
        Book book = modelMapper.map(request, Book.class);

        book.setAuthors(List.of());
        book.setCategories(List.of());

        return book;
    }

    public CreateBookUseCase.CreateBookCommand toCreateCommand(BookCreateRequest request) {
        return modelMapper.map(request, CreateBookUseCase.CreateBookCommand.class);
    }

    public UpdateBookUseCase.UpdateBookCommand toUpdateCommand(BookUpdateRequest request) {
        return modelMapper.map(request, UpdateBookUseCase.UpdateBookCommand.class);
    }

    public List<BookResponse> toResponseList(List<Book> books) {
        return books.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}