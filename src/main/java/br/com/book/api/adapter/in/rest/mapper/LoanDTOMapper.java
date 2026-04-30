package br.com.book.api.adapter.in.rest.mapper;

import br.com.book.api.domain.Loan;
import br.com.book.api.gen.model.LoanCreateRequest;
import br.com.book.api.gen.model.LoanResponse;
import br.com.book.api.application.port.in.loan.CreateLoanUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoanDTOMapper {

    @Inject
    ModelMapper modelMapper;

    // Domain → Response
    public LoanResponse toResponse(Loan loan) {
        LoanResponse response = modelMapper.map(loan, LoanResponse.class);

        // Campos que não estão diretamente no Domain
        if (loan.getUser() != null) {
            response.setUserId(loan.getUser().getId());
        }
        if (loan.getBook() != null) {
            response.setBookId(loan.getBook().getId());
        }

        return response;
    }

    // CreateRequest → Domain
    public Loan toDomain(LoanCreateRequest request) {
        return modelMapper.map(request, Loan.class);
    }

    // CreateRequest → CreateCommand
    public CreateLoanUseCase.CreateLoanCommand toCreateCommand(LoanCreateRequest request) {
        return modelMapper.map(request, CreateLoanUseCase.CreateLoanCommand.class);
    }

    // Lista Domain → Lista Response
    public List<LoanResponse> toResponseList(List<Loan> loans) {
        return loans.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}