package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.ReviewDTOMapper;
import br.com.book.api.application.port.in.review.*;
import br.com.book.api.domain.Review;
import br.com.book.api.domain.User;
import br.com.book.api.domain.Book;
import br.com.book.api.gen.model.GetAverageRating200Response;
import br.com.book.api.gen.model.ReviewCreateRequest;
import br.com.book.api.gen.model.ReviewResponse;
import br.com.book.api.gen.model.ReviewUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewResourceTest {

    @Mock
    private CreateReviewUseCase createReviewUseCase;

    @Mock
    private GetReviewUseCase getReviewUseCase;

    @Mock
    private ListReviewsByBookUseCase listReviewsByBookUseCase;

    @Mock
    private UpdateReviewUseCase updateReviewUseCase;

    @Mock
    private DeleteReviewUseCase deleteReviewUseCase;

    @Mock
    private ReviewDTOMapper mapper;

    @InjectMocks
    private ReviewResource reviewResource;

    private Review review;
    private User user;
    private Book book;
    private ReviewResponse reviewResponse;
    private ReviewCreateRequest createRequest;
    private ReviewUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("João Silva");

        book = new Book();
        book.setId(1L);
        book.setTitle("Dom Casmurro");

        review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setBook(book);
        review.setRating(5);
        review.setComment("Excelente livro!");

        reviewResponse = new ReviewResponse();
        reviewResponse.setId(1L);
        reviewResponse.setRating(5);
        reviewResponse.setComment("Excelente livro!");

        createRequest = new ReviewCreateRequest();
        createRequest.setUserId(1L);
        createRequest.setBookId(1L);
        createRequest.setRating(5);
        createRequest.setComment("Excelente livro!");

        updateRequest = new ReviewUpdateRequest();
        updateRequest.setRating(4);
        updateRequest.setComment("Bom livro, mas esperava mais");
    }

    // ========== CREATE REVIEW TESTS ==========

    @Test
    void createReview_ShouldReturnReviewResponse_WhenSuccessful() {
        // Arrange
        CreateReviewUseCase.CreateReviewCommand command = mock(CreateReviewUseCase.CreateReviewCommand.class);
        when(mapper.toCreateCommand(createRequest)).thenReturn(command);
        when(createReviewUseCase.execute(command)).thenReturn(review);
        when(mapper.toResponse(review)).thenReturn(reviewResponse);

        // Act
        ReviewResponse response = reviewResource.createReview(createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(5, response.getRating());
        assertEquals("Excelente livro!", response.getComment());
        verify(createReviewUseCase).execute(command);
        verify(mapper).toResponse(review);
    }

    // ========== GET REVIEW BY ID TESTS ==========

    @Test
    void getReviewById_ShouldReturnReviewResponse_WhenReviewExists() {
        // Arrange
        when(getReviewUseCase.findById(1L)).thenReturn(review);
        when(mapper.toResponse(review)).thenReturn(reviewResponse);

        // Act
        ReviewResponse response = reviewResource.getReviewById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(getReviewUseCase).findById(1L);
    }

    @Test
    void getReviewById_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        reviewResource.getReviewById(null);

        // Assert
        verify(getReviewUseCase).findById(null);
    }

    // ========== GET REVIEWS BY BOOK TESTS ==========

    @Test
    void getReviewsByBook_ShouldReturnListOfReviews_WhenBookHasReviews() {
        // Arrange
        Long bookId = 1L;
        List<Review> reviews = Arrays.asList(review);
        List<ReviewResponse> responses = Arrays.asList(reviewResponse);
        when(listReviewsByBookUseCase.findByBook(bookId)).thenReturn(reviews);
        when(mapper.toResponseList(reviews)).thenReturn(responses);

        // Act
        List<ReviewResponse> result = reviewResource.getReviewsByBook(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listReviewsByBookUseCase).findByBook(bookId);
    }

    @Test
    void getReviewsByBook_ShouldReturnEmptyList_WhenBookHasNoReviews() {
        // Arrange
        Long bookId = 1L;
        List<Review> reviews = Arrays.asList();
        List<ReviewResponse> responses = Arrays.asList();
        when(listReviewsByBookUseCase.findByBook(bookId)).thenReturn(reviews);
        when(mapper.toResponseList(reviews)).thenReturn(responses);

        // Act
        List<ReviewResponse> result = reviewResource.getReviewsByBook(bookId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(listReviewsByBookUseCase).findByBook(bookId);
    }

    @Test
    void getReviewsByBook_WithNullBookId_ShouldCallUseCaseWithNull() {
        // Act
        reviewResource.getReviewsByBook(null);

        // Assert
        verify(listReviewsByBookUseCase).findByBook(null);
    }

    // ========== GET AVERAGE RATING TESTS ==========

    @Test
    void getAverageRating_ShouldReturnAverageRatingResponse_WhenBookHasReviews() {
        // Arrange
        Long bookId = 1L;
        Double averageRating = 4.5;
        when(listReviewsByBookUseCase.getAverageRating(bookId)).thenReturn(averageRating);

        // Act
        GetAverageRating200Response response = reviewResource.getAverageRating(bookId);

        // Assert
        assertNotNull(response);
        assertEquals(averageRating, response.getAverageRating());
        verify(listReviewsByBookUseCase).getAverageRating(bookId);
    }

    @Test
    void getAverageRating_ShouldReturnZero_WhenBookHasNoReviews() {
        // Arrange
        Long bookId = 1L;
        Double averageRating = 0.0;
        when(listReviewsByBookUseCase.getAverageRating(bookId)).thenReturn(averageRating);

        // Act
        GetAverageRating200Response response = reviewResource.getAverageRating(bookId);

        // Assert
        assertNotNull(response);
        assertEquals(0.0, response.getAverageRating());
        verify(listReviewsByBookUseCase).getAverageRating(bookId);
    }

    @Test
    void getAverageRating_WithNullBookId_ShouldCallUseCaseWithNull() {
        // Act
        reviewResource.getAverageRating(null);

        // Assert
        verify(listReviewsByBookUseCase).getAverageRating(null);
    }

    // ========== UPDATE REVIEW TESTS ==========

    @Test
    void updateReview_ShouldReturnUpdatedReviewResponse_WhenSuccessful() {
        // Arrange
        UpdateReviewUseCase.UpdateReviewCommand command = mock(UpdateReviewUseCase.UpdateReviewCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);
        when(updateReviewUseCase.update(1L, command)).thenReturn(review);
        when(mapper.toResponse(review)).thenReturn(reviewResponse);

        // Act
        ReviewResponse response = reviewResource.updateReview(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(updateReviewUseCase).update(1L, command);
    }

    @Test
    void updateReview_WithNullId_ShouldCallUseCaseWithNull() {
        // Arrange
        UpdateReviewUseCase.UpdateReviewCommand command = mock(UpdateReviewUseCase.UpdateReviewCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);

        // Act
        reviewResource.updateReview(null, updateRequest);

        // Assert
        verify(updateReviewUseCase).update(null, command);
    }

    // ========== DELETE REVIEW TESTS ==========

    @Test
    void deleteReview_ShouldCallDeleteUseCase() {
        // Act
        reviewResource.deleteReview(1L);

        // Assert
        verify(deleteReviewUseCase).delete(1L);
    }

    @Test
    void deleteReview_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        reviewResource.deleteReview(null);

        // Assert
        verify(deleteReviewUseCase).delete(null);
    }

    // ========== EDGE CASES TESTS ==========

    @Test
    void getReviewById_ShouldHandleNonExistentId() {
        // Arrange
        when(getReviewUseCase.findById(999L)).thenReturn(null);
        when(mapper.toResponse(null)).thenReturn(null);

        // Act
        ReviewResponse response = reviewResource.getReviewById(999L);

        // Assert
        assertNull(response);
        verify(getReviewUseCase).findById(999L);
    }

    @Test
    void updateReview_ShouldPassCorrectParameters() {
        // Arrange
        Long reviewId = 5L;
        UpdateReviewUseCase.UpdateReviewCommand command = mock(UpdateReviewUseCase.UpdateReviewCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);
        when(updateReviewUseCase.update(reviewId, command)).thenReturn(review);
        when(mapper.toResponse(review)).thenReturn(reviewResponse);

        // Act
        reviewResource.updateReview(reviewId, updateRequest);

        // Assert
        verify(updateReviewUseCase).update(reviewId, command);
    }
}