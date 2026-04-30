package br.com.book.api.domain;

import br.com.book.api.enums.LoanStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan extends BaseDomain {

    private Long id;
    private User user;
    private Book book;
    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private BigDecimal fine;
    private LoanStatusEnum status;
    private String notes;


    public Loan(User user, Book book, LocalDateTime expectedReturnDate, String notes) {
        this.user = user;
        this.book = book;
        this.loanDate = LocalDateTime.now();
        this.expectedReturnDate = expectedReturnDate;
        this.notes = notes;
        this.fine = BigDecimal.ZERO;
        this.status = LoanStatusEnum.ACTIVE;
    }

    public void returnBook() {
        this.actualReturnDate = LocalDateTime.now();
        this.status = LoanStatusEnum.RETURNED;
        calculateFine();
    }

    public void calculateFine() {
        if (actualReturnDate == null) {
            this.fine = BigDecimal.ZERO;
            return;
        }

        if (actualReturnDate.isAfter(expectedReturnDate)) {
            long daysLate = Duration.between(expectedReturnDate, actualReturnDate).toDays();
            if (daysLate < 1) daysLate = 1; // qualquer atraso mínimo conta como 1 dia
            this.fine = BigDecimal.valueOf(daysLate * 2.00); // R$ 2,00 por dia
        } else {
            this.fine = BigDecimal.ZERO;
        }
    }

    public boolean isOverdue() {
        return status == LoanStatusEnum.ACTIVE && LocalDateTime.now().isAfter(expectedReturnDate);
    }

    public boolean isReturned() {
        return status == LoanStatusEnum.RETURNED;
    }

    public boolean isActive() {
        return status == LoanStatusEnum.ACTIVE;
    }

    public void markAsLost() {
        this.status = LoanStatusEnum.LOST;
    }

    public void markAsOverdue() {
        if (isOverdue()) {
            this.status = LoanStatusEnum.OVERDUE;
        }
    }

    public long getDaysLate() {
        if (actualReturnDate == null) {
            if (LocalDateTime.now().isAfter(expectedReturnDate)) {
                return Duration.between(expectedReturnDate, LocalDateTime.now()).toDays();
            }
            return 0;
        }

        if (actualReturnDate.isAfter(expectedReturnDate)) {
            return Duration.between(expectedReturnDate, actualReturnDate).toDays();
        }
        return 0;
    }

    public void renew(int extraDays) {
        if (isActive()) {
            this.expectedReturnDate = this.expectedReturnDate.plusDays(extraDays);
        } else {
            throw new IllegalStateException("Cannot renew a loan that is not active");
        }
    }
}