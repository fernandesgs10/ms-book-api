package br.com.book.api.application.port.in.loan;

import br.com.book.api.domain.Loan;
import java.util.List;

public interface ListLoansUseCase {

    List<Loan> findAll();

    List<Loan> findByUser(Long userId);

    List<Loan> findByBook(Long bookId);
}