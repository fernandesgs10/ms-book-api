package br.com.book.api.adapter.out.persistence.mapper;

import br.com.book.api.adapter.out.persistence.entity.ReviewEntity;
import br.com.book.api.domain.Review;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReviewMapper implements Serializable {


    @Inject
    ModelMapper modelMapper;

    @Inject
    BookMapper bookMapper;

    @Inject
    UserMapper userMapper;

    // Domain → Entity (com relacionamentos)
    public ReviewEntity toEntity(Review review) {
        if (review == null) return null;

        ReviewEntity entity = modelMapper.map(review, ReviewEntity.class);

        // Mapear relacionamentos manualmente
        if (review.getBook() != null) {
            entity.setBook(bookMapper.toEntity(review.getBook()));
        }
        if (review.getUser() != null) {
            entity.setUser(userMapper.toEntity(review.getUser()));
        }

        return entity;
    }

    // Entity → Domain (com relacionamentos)
    public Review toDomain(ReviewEntity entity) {
        if (entity == null) return null;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Review review = modelMapper.map(entity, Review.class);

        // Mapear relacionamentos manualmente
        if (entity.getBook() != null) {
            review.setBook(bookMapper.toDomain(entity.getBook()));
        }
        if (entity.getUser() != null) {
            review.setUser(userMapper.toDomain(entity.getUser()));
        }

        return review;
    }

    // Lista Domain → Lista Entity
    public List<ReviewEntity> toEntityList(List<Review> reviews) {
        if (reviews == null) return List.of();
        return reviews.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // Lista Entity → Lista Domain
    public List<Review> toDomainList(List<ReviewEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}