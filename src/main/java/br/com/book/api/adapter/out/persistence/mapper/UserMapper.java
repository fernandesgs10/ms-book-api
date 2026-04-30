package br.com.book.api.adapter.out.persistence.mapper;

import br.com.book.api.adapter.out.persistence.entity.AppUserEntity;
import br.com.book.api.domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserMapper implements Serializable {


    @Inject
    ModelMapper modelMapper;

    // Domain → Entity
    public AppUserEntity toEntity(User user) {
        if (user == null) return null;
        return modelMapper.map(user, AppUserEntity.class);
    }

    // Entity → Domain
    public User toDomain(AppUserEntity entity) {
        if (entity == null) return null;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(entity, User.class);
    }

    // Lista Domain → Lista Entity
    public List<AppUserEntity> toEntityList(List<User> users) {
        if (users == null) return List.of();
        return users.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // Lista Entity → Lista Domain
    public List<User> toDomainList(List<AppUserEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}