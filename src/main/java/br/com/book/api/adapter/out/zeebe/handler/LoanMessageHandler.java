package br.com.book.api.adapter.out.zeebe.handler;

import br.com.book.api.application.port.out.ZeebePort;
import io.camunda.zeebe.client.ZeebeClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class LoanMessageHandler {

    private static final Logger LOG = Logger.getLogger(LoanMessageHandler.class.getName());

    @Inject
    ZeebeClient zeebeClient;

    @Inject
    ZeebePort zeebePort;

    @PostConstruct
    public void subscribeToMessages() {
        LOG.info("Subscribing to loan messages...");

        // Handler para mensagem "loan-approved"
        zeebeClient.newWorker()
                .jobType("loan-approved-message")
                .handler((jobClient, job) -> {
                    Map<String, Object> variables = job.getVariablesAsMap();
                    LOG.info("Received loan-approved message: " + variables);

                    String correlationId = (String) variables.get("correlationId");
                    Long loanId = ((Number) variables.get("loanId")).longValue();

                    // Publica a mensagem correlacionada
                    zeebePort.publishMessage("loan-approved", correlationId, variables);

                    // Completa o job
                    zeebePort.completeTask(job.getKey(), Map.of("messageProcessed", true));
                })
                .open();

        LOG.info("Subscribed to loan messages successfully");
    }
}