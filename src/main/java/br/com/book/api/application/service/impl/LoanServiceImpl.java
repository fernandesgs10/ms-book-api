package br.com.book.api.application.service.impl;

import br.com.book.api.application.port.in.loan.*;
import br.com.book.api.application.port.out.LoanRepositoryPort;
import br.com.book.api.application.port.out.BookRepositoryPort;
import br.com.book.api.application.port.out.UserRepositoryPort;
import br.com.book.api.domain.Loan;
import br.com.book.api.domain.Book;
import br.com.book.api.domain.User;
import br.com.book.api.shared.exception.ResourceNotFoundException;
import br.com.book.api.shared.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class LoanServiceImpl implements
        CreateLoanUseCase,
        GetLoanUseCase,
        ListLoansUseCase,
        ReturnLoanUseCase,
        ListOverdueLoansUseCase {

    @Inject
    LoanRepositoryPort loanRepositoryPort;

    @Inject
    BookRepositoryPort bookRepositoryPort;

    @Inject
    UserRepositoryPort userRepositoryPort;

    @Override
    public Loan execute(CreateLoanCommand command) {
        User user = userRepositoryPort.findUserById(command.getUserId())  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("User", command.getUserId()));

        Book book = bookRepositoryPort.findBookById(command.getBookId())  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Book", command.getBookId()));

        if (!book.isAvailable()) {
            throw new BusinessException("Book is not available for loan: " + book.getTitle());
        }

//        if (!user.canBorrow()) {
//            throw new BusinessException("User cannot borrow more books");
//        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());
        loan.setExpectedReturnDate(command.getExpectedReturnDate());
        loan.setNotes(command.getNotes());

        book.decrementStock(1);
        bookRepositoryPort.save(book);

        return loanRepositoryPort.save(loan);
    }

    @Override
    public Loan findById(Long id) {
        return loanRepositoryPort.findLoanById(id)  // ← verifique este método
                .orElseThrow(() -> new ResourceNotFoundException("Loan", id));
    }

    @Override
    public List<Loan> findAll() {
        return loanRepositoryPort.findAllLoans();  // ← verifique este método
    }

    @Override
    public List<Loan> findByUser(Long userId) {
        if (userId == null) {
            return findAll();
        }
        return loanRepositoryPort.findLoansByUserId(userId);  // ← verifique este método
    }

    @Override
    public List<Loan> findByBook(Long bookId) {
        if (bookId == null) {
            return findAll();
        }
        return loanRepositoryPort.findLoansByBookId(bookId);  // ← verifique este método
    }

    @Override
    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepositoryPort.findLoanById(loanId)  // ← verifique este método
                .orElseThrow(() -> new ResourceNotFoundException("Loan", loanId));

        loan.returnBook();

        Book book = loan.getBook();
        book.incrementStock(1);
        bookRepositoryPort.save(book);

        return loanRepositoryPort.save(loan);
    }

    @Override
    public List<Loan> findOverdue() {
        return loanRepositoryPort.findOverdueLoans();  // ← verifique este método
    }
}