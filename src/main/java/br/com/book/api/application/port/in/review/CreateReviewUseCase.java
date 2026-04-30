package br.com.book.api.application.port.in.review;

import br.com.book.api.domain.Review;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateReviewUseCase {

    Review execute(@Valid CreateReviewCommand command);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateReviewCommand {
        private Long userId;
        private Long bookId;
        private Integer rating;
        private String comment;
    }
}