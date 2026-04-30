package br.com.book.api.application.service.impl;

import br.com.book.api.application.port.in.author.CreateAuthorUseCase;
import br.com.book.api.application.port.in.author.GetAuthorUseCase;
import br.com.book.api.application.port.in.author.ListAuthorsUseCase;
import br.com.book.api.application.port.in.author.UpdateAuthorUseCase;
import br.com.book.api.application.port.in.author.DeleteAuthorUseCase;
import br.com.book.api.application.port.out.AuthorRepositoryPort;

import br.com.book.api.domain.Author;
import br.com.book.api.shared.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class AuthorServiceImpl implements
        CreateAuthorUseCase,
        GetAuthorUseCase,
        ListAuthorsUseCase,
        UpdateAuthorUseCase,
        DeleteAuthorUseCase {

    @Inject
    AuthorRepositoryPort authorRepositoryPort;

    @Override
    public Author execute(CreateAuthorCommand command) {
        Author author = new Author();
        author.setName(command.getName());
        author.setNationality(command.getNationality());
        author.setBirthDate(command.getBirthDate());
        author.setBiography(command.getBiography());

        return authorRepositoryPort.save(author);
    }

    @Override
    public void delete(Long id) {
        if (!authorRepositoryPort.existsAuthorById(id)) {  // ← corrigido
            throw new ResourceNotFoundException("Author", id);
        }
        authorRepositoryPort.deleteAuthorById(id);  // ← corrigido
    }

    @Override
    public List<Author> execute() {
        return authorRepositoryPort.findAllAuthors();  // ← corrigido
    }

    @Override
    public List<Author> execute(String name) {
        if (name == null || name.isBlank()) {
            return execute();
        }
        return authorRepositoryPort.findAuthorsByName(name);  // ← corrigido
    }

    @Override
    public List<Author> executeByNationality(String nationality) {
        if (nationality == null || nationality.isBlank()) {
            return execute();
        }
        return authorRepositoryPort.findAuthorsByNationality(nationality);  // ← corrigido
    }

    @Override
    public Author execute(Long id, UpdateAuthorCommand command) {
        Author author = authorRepositoryPort.findAuthorById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));

        author.setName(command.getName());
        author.setNationality(command.getNationality());
        author.setBirthDate(command.getBirthDate());
        author.setBiography(command.getBiography());

        return authorRepositoryPort.save(author);
    }

    @Override
    public Author findById(Long id) {
        return authorRepositoryPort.findAuthorById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }
}