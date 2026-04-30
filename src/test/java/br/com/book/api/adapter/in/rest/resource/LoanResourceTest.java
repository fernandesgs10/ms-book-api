package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.LoanDTOMapper;
import br.com.book.api.adapter.out.zeebe.publisher.LoanEventPublisher;
import br.com.book.api.application.port.in.loan.*;
import br.com.book.api.domain.Loan;
import br.com.book.api.domain.User;
import br.com.book.api.domain.Book;
import br.com.book.api.gen.model.CreateLoan202Response;
import br.com.book.api.gen.model.LoanCreateRequest;
import br.com.book.api.gen.model.LoanResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanResourceTest {

    @Mock
    private CreateLoanUseCase createLoanUseCase;

    @Mock
    private GetLoanUseCase getLoanUseCase;

    @Mock
    private ListLoansUseCase listLoansUseCase;

    @Mock
    private ReturnLoanUseCase returnLoanUseCase;

    @Mock
    private ListOverdueLoansUseCase listOverdueLoansUseCase;

    @Mock
    private LoanEventPublisher loanEventPublisher;

    @Mock
    private LoanDTOMapper mapper;

    @InjectMocks
    private LoanResource loanResource;

    private Loan loan;
    private User user;
    private Book book;
    private LoanResponse loanResponse;
    private LoanCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("João Silva");

        book = new Book();
        book.setId(1L);
        book.setTitle("Dom Casmurro");

        loan = new Loan();
        loan.setId(1L);
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());
        loan.setExpectedReturnDate(LocalDateTime.now().plusDays(7));

        loanResponse = new LoanResponse();
        loanResponse.setId(1L);

        createRequest = new LoanCreateRequest();
        createRequest.setUserId(1L);
        createRequest.setBookId(1L);
    }

    // ========== CREATE LOAN TESTS ==========

    @Test
    void createLoan_ShouldReturnCreateLoan202Response_WhenCalled() {
        // Arrange
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> bookIdCaptor = ArgumentCaptor.forClass(Long.class);

        // Act
        CreateLoan202Response response = loanResource.createLoan(createRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getProcessId());
        assertTrue(response.getProcessId().startsWith("loan-process-"));
        assertEquals("Loan request received and is being processed", response.getMessage());

        verify(loanEventPublisher).startLoanProcess(
                userIdCaptor.capture(),
                bookIdCaptor.capture(),
                isNull(),
                isNull()
        );

        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(1L, bookIdCaptor.getValue());
    }

    @Test
    void createLoan_ShouldGenerateUniqueProcessId_WhenCalledMultipleTimes() {
        // Act
        CreateLoan202Response response1 = loanResource.createLoan(createRequest);
        CreateLoan202Response response2 = loanResource.createLoan(createRequest);

        // Assert
        assertNotEquals(response1.getProcessId(), response2.getProcessId());
    }

    // ========== GET LOAN BY ID TESTS ==========

    @Test
    void getLoanById_ShouldReturnLoanResponse_WhenLoanExists() {
        // Arrange
        when(getLoanUseCase.findById(1L)).thenReturn(loan);
        when(mapper.toResponse(loan)).thenReturn(loanResponse);

        // Act
        LoanResponse response = loanResource.getLoanById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(getLoanUseCase).findById(1L);
    }

    @Test
    void getLoanById_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        loanResource.getLoanById(null);

        // Assert
        verify(getLoanUseCase).findById(null);
    }

    // ========== GET ALL LOANS TESTS ==========

    @Test
    void getAllLoans_ShouldReturnListOfLoans_WhenNoFilters() {
        // Arrange
        List<Loan> loans = Arrays.asList(loan);
        List<LoanResponse> responses = Arrays.asList(loanResponse);
        when(listLoansUseCase.findAll()).thenReturn(loans);
        when(mapper.toResponseList(loans)).thenReturn(responses);

        // Act
        List<LoanResponse> result = loanResource.getAllLoans(null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listLoansUseCase).findAll();
        verify(listLoansUseCase, never()).findByUser(any());
        verify(listLoansUseCase, never()).findByBook(any());
    }

    @Test
    void getAllLoans_ShouldReturnFilteredByUserId_WhenUserIdProvided() {
        // Arrange
        Long userId = 1L;
        List<Loan> loans = Arrays.asList(loan);
        List<LoanResponse> responses = Arrays.asList(loanResponse);
        when(listLoansUseCase.findByUser(userId)).thenReturn(loans);
        when(mapper.toResponseList(loans)).thenReturn(responses);

        // Act
        List<LoanResponse> result = loanResource.getAllLoans(userId, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listLoansUseCase).findByUser(userId);
        verify(listLoansUseCase, never()).findAll();
        verify(listLoansUseCase, never()).findByBook(any());
    }

    @Test
    void getAllLoans_ShouldReturnFilteredByBookId_WhenBookIdProvided() {
        // Arrange
        Long bookId = 1L;
        List<Loan> loans = Arrays.asList(loan);
        List<LoanResponse> responses = Arrays.asList(loanResponse);
        when(listLoansUseCase.findByBook(bookId)).thenReturn(loans);
        when(mapper.toResponseList(loans)).thenReturn(responses);

        // Act
        List<LoanResponse> result = loanResource.getAllLoans(null, bookId, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listLoansUseCase).findByBook(bookId);
        verify(listLoansUseCase, never()).findAll();
        verify(listLoansUseCase, never()).findByUser(any());
    }

    @Test
    void getAllLoans_WithUserIdAndBookId_ShouldPrioritizeUserId() {
        // Arrange
        Long userId = 1L;
        Long bookId = 2L;
        List<Loan> loans = Arrays.asList(loan);
        List<LoanResponse> responses = Arrays.asList(loanResponse);
        when(listLoansUseCase.findByUser(userId)).thenReturn(loans);
        when(mapper.toResponseList(loans)).thenReturn(responses);

        // Act
        List<LoanResponse> result = loanResource.getAllLoans(userId, bookId, null);

        // Assert
        assertNotNull(result);
        verify(listLoansUseCase).findByUser(userId);
        verify(listLoansUseCase, never()).findByBook(any());
    }

    // ========== GET OVERDUE LOANS TESTS ==========

    @Test
    void getOverdueLoans_ShouldReturnListOfOverdueLoans() {
        // Arrange
        List<Loan> overdueLoans = Arrays.asList(loan);
        List<LoanResponse> responses = Arrays.asList(loanResponse);
        when(listOverdueLoansUseCase.findOverdue()).thenReturn(overdueLoans);
        when(mapper.toResponseList(overdueLoans)).thenReturn(responses);

        // Act
        List<LoanResponse> result = loanResource.getOverdueLoans();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listOverdueLoansUseCase).findOverdue();
    }

    // ========== RETURN LOAN TESTS ==========

    @Test
    void returnLoan_ShouldReturnLoanResponse_WhenSuccessful() {
        // Arrange
        when(returnLoanUseCase.returnLoan(1L)).thenReturn(loan);
        when(mapper.toResponse(loan)).thenReturn(loanResponse);

        // Act
        LoanResponse response = loanResource.returnLoan(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(returnLoanUseCase).returnLoan(1L);
    }

    @Test
    void returnLoan_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        loanResource.returnLoan(null);

        // Assert
        verify(returnLoanUseCase).returnLoan(null);
    }

    // ========== EDGE CASES TESTS ==========

    @Test
    void getAllLoans_WithEmptyFilters_ShouldReturnAllLoans() {
        // Arrange
        List<Loan> loans = Arrays.asList(loan);
        List<LoanResponse> responses = Arrays.asList(loanResponse);
        when(listLoansUseCase.findAll()).thenReturn(loans);
        when(mapper.toResponseList(loans)).thenReturn(responses);

        // Act
        List<LoanResponse> result = loanResource.getAllLoans(null, null, "");

        // Assert
        assertNotNull(result);
        verify(listLoansUseCase).findAll();
    }

    @Test
    void createLoan_ShouldPassNullForUserEmailAndBookTitle() {
        // Arrange
        ArgumentCaptor<String> userEmailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bookTitleCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        loanResource.createLoan(createRequest);

        // Assert
        verify(loanEventPublisher).startLoanProcess(
                anyLong(),
                anyLong(),
                userEmailCaptor.capture(),
                bookTitleCaptor.capture()
        );

        assertNull(userEmailCaptor.getValue());
        assertNull(bookTitleCaptor.getValue());
    }
}