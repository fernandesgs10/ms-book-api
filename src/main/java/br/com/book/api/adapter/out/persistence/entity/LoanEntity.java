package br.com.book.api.adapter.out.persistence.entity;

import br.com.book.api.enums.LoanStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loans")
public class LoanEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @NotNull(message = "Loan date is required")
    @Column(name = "loan_date", nullable = false)
    private LocalDateTime loanDate;

    @NotNull(message = "Expected return date is required")
    @Future(message = "Expected return date must be in the future")
    @Column(name = "expected_return_date", nullable = false)
    private LocalDateTime expectedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDate;

    @DecimalMin(value = "0.0", message = "Fine cannot be negative")
    private BigDecimal fine;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LoanStatusEnum status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        loanDate = LocalDateTime.now();
        status = LoanStatusEnum.ACTIVE;
        fine = BigDecimal.ZERO;
    }
}
