package br.com.book.api.adapter.out.persistence.mapper;

import br.com.book.api.adapter.out.persistence.entity.PublisherEntity;
import br.com.book.api.domain.Publisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PublisherMapper implements Serializable {


    @Inject
    ModelMapper modelMapper;

    // Domain → Entity
    public PublisherEntity toEntity(Publisher publisher) {
        if (publisher == null) return null;
        return modelMapper.map(publisher, PublisherEntity.class);
    }

    // Entity → Domain
    public Publisher toDomain(PublisherEntity entity) {
        if (entity == null) return null;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(entity, Publisher.class);
    }

    // Lista Domain → Lista Entity
    public List<PublisherEntity> toEntityList(List<Publisher> publishers) {
        if (publishers == null) return List.of();
        return publishers.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // Lista Entity → Lista Domain
    public List<Publisher> toDomainList(List<PublisherEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}