package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.AuthorDTOMapper;
import br.com.book.api.application.port.in.author.*;
import br.com.book.api.domain.Author;
import br.com.book.api.gen.api.AuthorsApi;
import br.com.book.api.gen.model.AuthorCreateRequest;
import br.com.book.api.gen.model.AuthorResponse;
import br.com.book.api.gen.model.AuthorUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class AuthorResource implements AuthorsApi {

    @Inject
    CreateAuthorUseCase createAuthorUseCase;

    @Inject
    GetAuthorUseCase getAuthorUseCase;

    @Inject
    ListAuthorsUseCase listAuthorsUseCase;

    @Inject
    UpdateAuthorUseCase updateAuthorUseCase;

    @Inject
    DeleteAuthorUseCase deleteAuthorUseCase;

    @Inject
    AuthorDTOMapper mapper;

    @Override
    public List<AuthorResponse> getAllAuthors(String name, String nationality) {
        if (name != null && !name.isBlank()) {
            return mapper.toResponseList(listAuthorsUseCase.execute(name));
        }
        if (nationality != null && !nationality.isBlank()) {
            return mapper.toResponseList(listAuthorsUseCase.executeByNationality(nationality));
        }
        return mapper.toResponseList(listAuthorsUseCase.execute());
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        Author author = getAuthorUseCase.findById(id);  // ← corrigido: findById
        return mapper.toResponse(author);
    }

    @Override
    public AuthorResponse createAuthor(AuthorCreateRequest authorCreateRequest) {  // ← retorna AuthorResponse, não Response
        CreateAuthorUseCase.CreateAuthorCommand command = mapper.toCreateCommand(authorCreateRequest);
        Author author = createAuthorUseCase.execute(command);
        return mapper.toResponse(author);
    }

    @Override
    public AuthorResponse updateAuthor(Long id, AuthorUpdateRequest authorUpdateRequest) {
        UpdateAuthorUseCase.UpdateAuthorCommand command = mapper.toUpdateCommand(authorUpdateRequest);
        Author author = updateAuthorUseCase.execute(id, command);
        return mapper.toResponse(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        deleteAuthorUseCase.delete(id);  // ← corrigido: delete
    }
}