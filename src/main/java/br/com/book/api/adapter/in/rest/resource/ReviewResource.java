package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.ReviewDTOMapper;
import br.com.book.api.application.port.in.review.*;
import br.com.book.api.domain.Review;
import br.com.book.api.gen.api.ReviewsApi;
import br.com.book.api.gen.model.GetAverageRating200Response;
import br.com.book.api.gen.model.ReviewCreateRequest;
import br.com.book.api.gen.model.ReviewResponse;
import br.com.book.api.gen.model.ReviewUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ReviewResource implements ReviewsApi {

    @Inject
    CreateReviewUseCase createReviewUseCase;

    @Inject
    GetReviewUseCase getReviewUseCase;

    @Inject
    ListReviewsByBookUseCase listReviewsByBookUseCase;

    @Inject
    UpdateReviewUseCase updateReviewUseCase;

    @Inject
    DeleteReviewUseCase deleteReviewUseCase;

    @Inject
    ReviewDTOMapper mapper;

    @Override
    public ReviewResponse createReview(ReviewCreateRequest reviewCreateRequest) {
        CreateReviewUseCase.CreateReviewCommand command = mapper.toCreateCommand(reviewCreateRequest);
        Review review = createReviewUseCase.execute(command);
        return mapper.toResponse(review);
    }

    @Override
    public void deleteReview(Long id) {
        deleteReviewUseCase.delete(id);
    }

    @Override
    public GetAverageRating200Response getAverageRating(Long bookId) {
        Double average = listReviewsByBookUseCase.getAverageRating(bookId);

        GetAverageRating200Response response = new GetAverageRating200Response();
        response.setAverageRating(average);

        return response;
    }

    @Override
    public ReviewResponse getReviewById(Long id) {
        Review review = getReviewUseCase.findById(id);
        return mapper.toResponse(review);
    }

    @Override
    public List<ReviewResponse> getReviewsByBook(Long bookId) {
        return mapper.toResponseList(listReviewsByBookUseCase.findByBook(bookId));
    }

    @Override
    public ReviewResponse updateReview(Long id, ReviewUpdateRequest reviewUpdateRequest) {
        UpdateReviewUseCase.UpdateReviewCommand command = mapper.toUpdateCommand(reviewUpdateRequest);
        Review review = updateReviewUseCase.update(id, command);
        return mapper.toResponse(review);
    }
}