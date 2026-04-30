package br.com.book.api.domain;

import br.com.book.api.enums.LoanStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseDomain {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private LocalDateTime registrationDate;
    private Boolean active;
    private List<Loan> loans = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();

    public void activate() {
        this.active = true;
    }


    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active != null && active;
    }

    public boolean canBorrow() {
        if (!isActive()) {
            return false;
        }

        if (loans == null) {
            return true;
        }

        long activeLoans = loans.stream()
                .filter(loan -> loan.getStatus() != null && loan.getStatus() == LoanStatusEnum.ACTIVE)
                .count();

        return activeLoans < 5;
    }

    public long getActiveLoansCount() {
        if (loans == null) {
            return 0;
        }

        return loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatusEnum.ACTIVE)
                .count();
    }

    public void addLoan(Loan loan) {
        if (this.loans == null) {
            this.loans = new ArrayList<>();
        }
        if (loan != null && !this.loans.contains(loan)) {
            this.loans.add(loan);
            loan.setUser(this);
        }
    }

    public void removeLoan(Loan loan) {
        if (this.loans != null && loan != null) {
            this.loans.remove(loan);
            loan.setUser(null);
        }
    }

    public void addReview(Review review) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        if (review != null && !this.reviews.contains(review)) {
            this.reviews.add(review);
            review.setUser(this);
        }
    }

    public void removeReview(Review review) {
        if (this.reviews != null && review != null) {
            this.reviews.remove(review);
            review.setUser(null);
        }
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
        if (this.registrationDate == null) {
            this.registrationDate = LocalDateTime.now();
        }
    }
}