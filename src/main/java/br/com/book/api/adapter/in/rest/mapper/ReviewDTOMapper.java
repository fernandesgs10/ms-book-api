package br.com.book.api.adapter.in.rest.mapper;

import br.com.book.api.domain.Review;
import br.com.book.api.gen.model.ReviewCreateRequest;
import br.com.book.api.gen.model.ReviewResponse;
import br.com.book.api.gen.model.ReviewUpdateRequest;
import br.com.book.api.application.port.in.review.CreateReviewUseCase;
import br.com.book.api.application.port.in.review.UpdateReviewUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReviewDTOMapper {

    @Inject
    ModelMapper modelMapper;

    // Domain → Response
    public ReviewResponse toResponse(Review review) {
        ReviewResponse response = modelMapper.map(review, ReviewResponse.class);

        // Campos que não estão diretamente no Domain
        if (review.getUser() != null) {
            response.setUserId(review.getUser().getId());
            response.setUserName(review.getUser().getName());
        }
        if (review.getBook() != null) {
            response.setBookId(review.getBook().getId());
            response.setBookTitle(review.getBook().getTitle());
        }

        return response;
    }

    // CreateRequest → Domain
    public Review toDomain(ReviewCreateRequest request) {
        return modelMapper.map(request, Review.class);
    }

    // CreateRequest → CreateCommand
    public CreateReviewUseCase.CreateReviewCommand toCreateCommand(ReviewCreateRequest request) {
        return modelMapper.map(request, CreateReviewUseCase.CreateReviewCommand.class);
    }

    // UpdateRequest → UpdateCommand
    public UpdateReviewUseCase.UpdateReviewCommand toUpdateCommand(ReviewUpdateRequest request) {
        return modelMapper.map(request, UpdateReviewUseCase.UpdateReviewCommand.class);
    }

    // Lista Domain → Lista Response
    public List<ReviewResponse> toResponseList(List<Review> reviews) {
        return reviews.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}