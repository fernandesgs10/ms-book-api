package br.com.book.api.adapter.out.persistence.repository;

import br.com.book.api.application.port.out.ReviewRepositoryPort;

import br.com.book.api.adapter.out.persistence.entity.ReviewEntity;
import br.com.book.api.adapter.out.persistence.mapper.ReviewMapper;
import br.com.book.api.domain.Review;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ReviewRepositoryAdapter implements ReviewRepositoryPort, PanacheRepository<ReviewEntity> {

    @Inject
    ReviewMapper reviewMapper;

    @Override
    public List<Review> findAllReviews() {
        return reviewMapper.toDomainList(listAll());
    }

    @Override
    public Optional<Review> findReviewById(Long id) {
        return findByIdOptional(id).map(reviewMapper::toDomain);
    }

    @Override
    public List<Review> findReviewsByBookId(Long bookId) {
        return reviewMapper.toDomainList(
                find("book.id = :bookId ORDER BY reviewDate DESC",
                        Parameters.with("bookId", bookId)).list()
        );
    }

    @Override
    public List<Review> findReviewsByUserId(Long userId) {
        return reviewMapper.toDomainList(
                find("user.id = :userId ORDER BY reviewDate DESC",
                        Parameters.with("userId", userId)).list()
        );
    }

    @Override
    public Optional<Review> findReviewByUserAndBook(Long userId, Long bookId) {
        return find("user.id = :userId AND book.id = :bookId",
                Parameters.with("userId", userId).and("bookId", bookId))
                .firstResultOptional()
                .map(reviewMapper::toDomain);
    }

    @Override
    public List<Review> findReviewsByRatingGreaterThan(Integer minRating) {
        return reviewMapper.toDomainList(
                find("rating >= :minRating ORDER BY rating DESC",
                        Parameters.with("minRating", minRating)).list()
        );
    }

    @Override
    public List<Review> findReviewsByRating(Integer rating) {
        return reviewMapper.toDomainList(
                find("rating = :rating", Parameters.with("rating", rating)).list()
        );
    }

    @Override
    public List<Review> findReviewsWithComment() {
        return reviewMapper.toDomainList(
                find("comment IS NOT NULL AND comment != '' ORDER BY reviewDate DESC").list()
        );
    }

    @Override
    public Review save(Review review) {
        ReviewEntity entity = reviewMapper.toEntity(review);
        persist(entity);
        return reviewMapper.toDomain(entity);
    }

    @Override
    public void deleteReviewById(Long id) {
        deleteById(id);
    }

    @Override
    public void deleteReviewByUserAndBook(Long userId, Long bookId) {
        find("user.id = :userId AND book.id = :bookId",
                Parameters.with("userId", userId).and("bookId", bookId))
                .list()
                .forEach(this::delete);
    }

    @Override
    public boolean existsReviewById(Long id) {
        return findByIdOptional(id).isPresent();
    }

    @Override
    public boolean existsReviewByUserAndBook(Long userId, Long bookId) {
        return count("user.id = :userId AND book.id = :bookId",
                Parameters.with("userId", userId).and("bookId", bookId)) > 0;
    }

    @Override
    public Double getAverageRatingByBookId(Long bookId) {
        Double avg = getEntityManager()
                .createQuery("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.book.id = :bookId", Double.class)
                .setParameter("bookId", bookId)
                .getSingleResult();
        return avg != null ? avg : 0.0;
    }

    @Override
    public long countReviewsByBookId(Long bookId) {
        return count("book.id = :bookId", Parameters.with("bookId", bookId));
    }
}