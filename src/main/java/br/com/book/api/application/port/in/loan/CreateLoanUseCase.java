package br.com.book.api.application.port.in.loan;

import br.com.book.api.domain.Loan;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public interface CreateLoanUseCase {

    Loan execute(@Valid CreateLoanCommand command);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateLoanCommand {
        private Long userId;
        private Long bookId;
        private LocalDateTime expectedReturnDate;
        private String notes;
    }
}