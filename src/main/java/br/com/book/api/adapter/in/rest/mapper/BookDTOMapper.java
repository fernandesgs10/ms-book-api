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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookDTOMapper {

    @Inject
    ModelMapper modelMapper;

    @PostConstruct
    void configure() {
        // Configuração para evitar problemas com listas
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true);

        // Mapeamento específico para BookCreateRequest → Book
        modelMapper.createTypeMap(BookCreateRequest.class, Book.class)
                .setProvider(request -> new Book())
                .setPostConverter(context -> {
                    Book book = context.getDestination();
                    if (book.getAuthors() == null) {
                        book.setAuthors(new ArrayList<>());
                    }
                    if (book.getCategories() == null) {
                        book.setCategories(new ArrayList<>());
                    }
                    return book;
                });
    }

    public BookResponse toResponse(Book book) {
        return modelMapper.map(book, BookResponse.class);
    }

    public Book toDomain(BookCreateRequest request) {
        return modelMapper.map(request, Book.class);
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