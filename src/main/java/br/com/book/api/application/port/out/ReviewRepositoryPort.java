package br.com.book.api.application.port.out;

import br.com.book.api.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepositoryPort {

    // Buscas
    List<Review> findAllReviews();

    Optional<Review> findReviewById(Long id);

    List<Review> findReviewsByBookId(Long bookId);

    List<Review> findReviewsByUserId(Long userId);

    Optional<Review> findReviewByUserAndBook(Long userId, Long bookId);

    List<Review> findReviewsByRatingGreaterThan(Integer minRating);

    List<Review> findReviewsByRating(Integer rating);

    List<Review> findReviewsWithComment();

    // Operações
    Review save(Review review);

    void deleteReviewById(Long id);

    void deleteReviewByUserAndBook(Long userId, Long bookId);

    // Validações
    boolean existsReviewById(Long id);

    boolean existsReviewByUserAndBook(Long userId, Long bookId);

    // Métodos de agregação
    Double getAverageRatingByBookId(Long bookId);

    long countReviewsByBookId(Long bookId);
}