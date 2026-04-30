package br.com.book.api.adapter.out.zeebe.publisher;

import br.com.book.api.application.port.out.ZeebePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class LoanEventPublisher {

    private static final Logger LOG = Logger.getLogger(LoanEventPublisher.class.getName());

    @Inject
    ZeebePort zeebePort;

    public void startLoanProcess(Long userId, Long bookId, String userEmail, String bookTitle) {
        LOG.info("Starting loan process for user: " + userId + " book: " + bookId);

        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", userId);
        variables.put("bookId", bookId);
        variables.put("userEmail", userEmail);
        variables.put("bookTitle", bookTitle);
        variables.put("loanValid", false);

        zeebePort.startProcess("book-loan-process", variables);
    }
}