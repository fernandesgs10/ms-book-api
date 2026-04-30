package br.com.book.api.application.port.in.loan;

import br.com.book.api.domain.Loan;
import java.util.List;

public interface ListOverdueLoansUseCase {

    List<Loan> findOverdue();
}