package br.com.book.api.adapter.out.persistence.mapper;

import br.com.book.api.adapter.out.persistence.entity.AuthorEntity;
import br.com.book.api.domain.Author;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthorMapper implements Serializable {


    @Inject
    ModelMapper modelMapper;

    // Domain → Entity
    public AuthorEntity toEntity(Author author) {
        if (author == null) return null;
        return modelMapper.map(author, AuthorEntity.class);
    }

    // Entity → Domain
    public Author toDomain(AuthorEntity entity) {
        if (entity == null) return null;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(entity, Author.class);
    }

    // Lista Domain → Lista Entity
    public List<AuthorEntity> toEntityList(List<Author> authors) {
        if (authors == null) return List.of();
        return authors.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // Lista Entity → Lista Domain
    public List<Author> toDomainList(List<AuthorEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}