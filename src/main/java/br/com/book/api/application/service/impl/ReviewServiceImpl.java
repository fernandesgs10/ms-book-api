package br.com.book.api.application.service.impl;

import br.com.book.api.application.port.in.review.*;
import br.com.book.api.application.port.out.ReviewRepositoryPort;
import br.com.book.api.application.port.out.BookRepositoryPort;
import br.com.book.api.application.port.out.UserRepositoryPort;
import br.com.book.api.domain.Review;
import br.com.book.api.domain.Book;
import br.com.book.api.domain.User;
import br.com.book.api.shared.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class ReviewServiceImpl implements
        CreateReviewUseCase,
        GetReviewUseCase,
        ListReviewsByBookUseCase,
        UpdateReviewUseCase,
        DeleteReviewUseCase {

    @Inject
    ReviewRepositoryPort reviewRepositoryPort;

    @Inject
    BookRepositoryPort bookRepositoryPort;

    @Inject
    UserRepositoryPort userRepositoryPort;

    @Override
    public Review execute(CreateReviewCommand command) {
        User user = userRepositoryPort.findUserById(command.getUserId())  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("User", command.getUserId()));

        Book book = bookRepositoryPort.findBookById(command.getBookId())  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Book", command.getBookId()));

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(command.getRating());
        review.setComment(command.getComment());

        return reviewRepositoryPort.save(review);
    }

    @Override
    public Review findById(Long id) {
        return reviewRepositoryPort.findReviewById(id)  // ← verifique este método
                .orElseThrow(() -> new ResourceNotFoundException("Review", id));
    }

    @Override
    public List<Review> findByBook(Long bookId) {
        return reviewRepositoryPort.findReviewsByBookId(bookId);  // ← verifique este método
    }

    @Override
    public Double getAverageRating(Long bookId) {
        return reviewRepositoryPort.getAverageRatingByBookId(bookId);
    }

    @Override
    public Review update(Long id, UpdateReviewCommand command) {
        Review review = reviewRepositoryPort.findReviewById(id)  // ← verifique este método
                .orElseThrow(() -> new ResourceNotFoundException("Review", id));

        review.setRating(command.getRating());
        review.setComment(command.getComment());

        return reviewRepositoryPort.save(review);
    }

    @Override
    public void delete(Long id) {
        if (!reviewRepositoryPort.existsReviewById(id)) {  // ← verifique este método
            throw new ResourceNotFoundException("Review", id);
        }
        reviewRepositoryPort.deleteReviewById(id);  // ← verifique este método
    }
}