package br.com.book.api.application.port.in.loan;

import br.com.book.api.domain.Loan;

public interface ReturnLoanUseCase {

    Loan returnLoan(Long loanId);
}