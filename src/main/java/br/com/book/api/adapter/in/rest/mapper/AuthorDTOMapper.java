package br.com.book.api.adapter.in.rest.mapper;

import br.com.book.api.domain.Author;
import br.com.book.api.gen.model.AuthorCreateRequest;
import br.com.book.api.gen.model.AuthorResponse;
import br.com.book.api.gen.model.AuthorUpdateRequest;
import br.com.book.api.application.port.in.author.CreateAuthorUseCase;
import br.com.book.api.application.port.in.author.UpdateAuthorUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthorDTOMapper {

    @Inject
    ModelMapper modelMapper;

    // Domain → Response
    public AuthorResponse toResponse(Author author) {
        return modelMapper.map(author, AuthorResponse.class);
    }

    // CreateRequest → Domain
    public Author toDomain(AuthorCreateRequest request) {
        return modelMapper.map(request, Author.class);
    }

    // CreateRequest → CreateCommand
    public CreateAuthorUseCase.CreateAuthorCommand toCreateCommand(AuthorCreateRequest request) {
        return modelMapper.map(request, CreateAuthorUseCase.CreateAuthorCommand.class);
    }

    // UpdateRequest → UpdateCommand
    public UpdateAuthorUseCase.UpdateAuthorCommand toUpdateCommand(AuthorUpdateRequest request) {
        return modelMapper.map(request, UpdateAuthorUseCase.UpdateAuthorCommand.class);
    }

    // Lista Domain → Lista Response
    public List<AuthorResponse> toResponseList(List<Author> authors) {
        return authors.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}