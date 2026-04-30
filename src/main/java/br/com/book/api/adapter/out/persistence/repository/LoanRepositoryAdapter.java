package br.com.book.api.adapter.out.persistence.repository;

import br.com.book.api.application.port.out.LoanRepositoryPort;
import br.com.book.api.adapter.out.persistence.entity.LoanEntity;
import br.com.book.api.adapter.out.persistence.mapper.LoanMapper;
import br.com.book.api.domain.Loan;
import br.com.book.api.enums.LoanStatusEnum;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LoanRepositoryAdapter implements LoanRepositoryPort, PanacheRepository<LoanEntity> {

    @Inject
    LoanMapper loanMapper;

    @Override
    public List<Loan> findAllLoans() {
        return loanMapper.toDomainList(listAll());
    }

    @Override
    public Optional<Loan> findLoanById(Long id) {
        return findByIdOptional(id).map(loanMapper::toDomain);
    }

    @Override
    public List<Loan> findLoansByUserId(Long userId) {
        return loanMapper.toDomainList(
                find("user.id = :userId ORDER BY loanDate DESC",
                        Parameters.with("userId", userId)).list()
        );
    }

    @Override
    public List<Loan> findLoansByBookId(Long bookId) {
        return loanMapper.toDomainList(
                find("book.id = :bookId ORDER BY loanDate DESC",
                        Parameters.with("bookId", bookId)).list()
        );
    }

    @Override
    public List<Loan> findLoansByUserIdAndStatus(Long userId, LoanStatusEnum status) {
        return loanMapper.toDomainList(
                find("user.id = :userId AND status = :status",
                        Parameters.with("userId", userId).and("status", status)).list()
        );
    }

    @Override
    public List<Loan> findActiveLoans() {
        return loanMapper.toDomainList(
                find("status = :status", Parameters.with("status", LoanStatusEnum.ACTIVE)).list()
        );
    }

    @Override
    public List<Loan> findOverdueLoans() {
        return loanMapper.toDomainList(
                find("status = :status AND expectedReturnDate < :now",
                        Parameters.with("status", LoanStatusEnum.ACTIVE)
                                .and("now", LocalDateTime.now())).list()
        );
    }

    @Override
    public List<Loan> findLoansByStatus(LoanStatusEnum status) {
        return loanMapper.toDomainList(
                find("status = :status", Parameters.with("status", status)).list()
        );
    }

    @Override
    public List<Loan> findLoansReturnedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return loanMapper.toDomainList(
                find("status = :status AND actualReturnDate BETWEEN :startDate AND :endDate",
                        Parameters.with("status", LoanStatusEnum.RETURNED)
                                .and("startDate", startDate)
                                .and("endDate", endDate)).list()
        );
    }

    @Override
    public List<Loan> findLoansWithFine() {
        return loanMapper.toDomainList(
                find("fine > 0 AND status != :status",
                        Parameters.with("status", LoanStatusEnum.RETURNED)).list()
        );
    }

    @Override
    public Loan save(Loan loan) {
        LoanEntity entity = loanMapper.toEntity(loan);
        persist(entity);
        return loanMapper.toDomain(entity);
    }

    @Override
    public void deleteLoanById(Long id) {
        deleteById(id);
    }

    @Override
    public boolean existsLoanById(Long id) {
        return findByIdOptional(id).isPresent();
    }

    @Override
    public boolean hasActiveLoanForBook(Long userId, Long bookId) {
        return count("user.id = :userId AND book.id = :bookId AND status = :status",
                Parameters.with("userId", userId)
                        .and("bookId", bookId)
                        .and("status", LoanStatusEnum.ACTIVE)) > 0;
    }

    @Override
    public long countActiveLoansByUserId(Long userId) {
        return count("user.id = :userId AND status = :status",
                Parameters.with("userId", userId)
                        .and("status", LoanStatusEnum.ACTIVE));
    }
}