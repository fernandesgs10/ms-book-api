package br.com.book.api.application.port.in.review;

import br.com.book.api.domain.Review;

public interface GetReviewUseCase {

    Review findById(Long id);
}