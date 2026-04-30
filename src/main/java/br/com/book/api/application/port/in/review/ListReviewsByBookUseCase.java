package br.com.book.api.application.port.in.review;

import br.com.book.api.domain.Review;
import java.util.List;

public interface ListReviewsByBookUseCase {

    List<Review> findByBook(Long bookId);

    Double getAverageRating(Long bookId);
}