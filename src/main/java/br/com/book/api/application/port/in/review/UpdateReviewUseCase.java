package br.com.book.api.application.port.in.review;

import br.com.book.api.domain.Review;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateReviewUseCase {

    Review update(Long id, @Valid UpdateReviewCommand command);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateReviewCommand {
        private Integer rating;
        private String comment;
    }
}