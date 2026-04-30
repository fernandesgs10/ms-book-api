package br.com.book.api.adapter.out.persistence.mapper;

import br.com.book.api.adapter.out.persistence.entity.CategoryEntity;
import br.com.book.api.domain.Category;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryMapper implements Serializable {


    @Inject
    ModelMapper modelMapper;

    // Domain → Entity
    public CategoryEntity toEntity(Category category) {
        if (category == null) return null;
        return modelMapper.map(category, CategoryEntity.class);
    }

    // Entity → Domain
    public Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(entity, Category.class);
    }

    // Lista Domain → Lista Entity
    public List<CategoryEntity> toEntityList(List<Category> categories) {
        if (categories == null) return List.of();
        return categories.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // Lista Entity → Lista Domain
    public List<Category> toDomainList(List<CategoryEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}