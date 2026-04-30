package br.com.book.api.application.port.out;

import br.com.book.api.domain.Loan;
import br.com.book.api.enums.LoanStatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanRepositoryPort {

    // Buscas
    List<Loan> findAllLoans();

    Optional<Loan> findLoanById(Long id);

    List<Loan> findLoansByUserId(Long userId);

    List<Loan> findLoansByBookId(Long bookId);

    List<Loan> findLoansByUserIdAndStatus(Long userId, LoanStatusEnum status);

    List<Loan> findActiveLoans();

    List<Loan> findOverdueLoans();

    List<Loan> findLoansByStatus(LoanStatusEnum status);

    List<Loan> findLoansReturnedBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Loan> findLoansWithFine();

    // Operações
    Loan save(Loan loan);

    void deleteLoanById(Long id);

    // Validações
    boolean existsLoanById(Long id);

    boolean hasActiveLoanForBook(Long userId, Long bookId);

    // Contagem
    long countActiveLoansByUserId(Long userId);
}