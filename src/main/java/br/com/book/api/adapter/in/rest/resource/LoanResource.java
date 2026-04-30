package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.LoanDTOMapper;
import br.com.book.api.adapter.out.zeebe.publisher.LoanEventPublisher;
import br.com.book.api.application.port.in.loan.*;
import br.com.book.api.domain.Loan;
import br.com.book.api.gen.api.LoansApi;
import br.com.book.api.gen.model.CreateLoan202Response;
import br.com.book.api.gen.model.LoanCreateRequest;
import br.com.book.api.gen.model.LoanResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LoanResource implements LoansApi {

    @Inject
    CreateLoanUseCase createLoanUseCase;

    @Inject
    GetLoanUseCase getLoanUseCase;

    @Inject
    ListLoansUseCase listLoansUseCase;

    @Inject
    ReturnLoanUseCase returnLoanUseCase;

    @Inject
    ListOverdueLoansUseCase listOverdueLoansUseCase;  // ← UseCase separado para empréstimos atrasados

    @Inject
    LoanEventPublisher loanEventPublisher;

    @Inject
    LoanDTOMapper mapper;

    @Override
    public CreateLoan202Response createLoan(LoanCreateRequest loanCreateRequest) {
        String processId = "loan-process-" + UUID.randomUUID().toString();

        loanEventPublisher.startLoanProcess(
                loanCreateRequest.getUserId(),
                loanCreateRequest.getBookId(),
                null,
                null
        );

        CreateLoan202Response response = new CreateLoan202Response();
        response.setMessage("Loan request received and is being processed");
        response.setProcessId(processId);

        return response;
    }

    @Override
    public List<LoanResponse> getAllLoans(Long userId, Long bookId, String status) {
        if (userId != null) {
            return mapper.toResponseList(listLoansUseCase.findByUser(userId));
        }
        if (bookId != null) {
            return mapper.toResponseList(listLoansUseCase.findByBook(bookId));
        }
        return mapper.toResponseList(listLoansUseCase.findAll());
    }

    @Override
    public LoanResponse getLoanById(Long id) {
        Loan loan = getLoanUseCase.findById(id);
        return mapper.toResponse(loan);
    }

    @Override
    public List<LoanResponse> getOverdueLoans() {
        // Usa o UseCase específico para empréstimos atrasados
        return mapper.toResponseList(listOverdueLoansUseCase.findOverdue());
    }

    @Override
    public LoanResponse returnLoan(Long id) {
        Loan loan = returnLoanUseCase.returnLoan(id);
        return mapper.toResponse(loan);
    }
}