package br.com.book.api.adapter.out.zeebe.worker;

import br.com.book.api.application.port.out.ZeebePort;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class NotifyUserWorker {

    private static final Logger LOG = Logger.getLogger(NotifyUserWorker.class.getName());

    @Inject
    ZeebeClient zeebeClient;

    @Inject
    ZeebePort zeebePort;

    private AutoCloseable workerSubscription;

    @PostConstruct
    public void startWorker() {
        LOG.info("Starting NotifyUserWorker...");

        workerSubscription = zeebeClient.newWorker()
                .jobType("notify-user")
                .handler((jobClient, job) -> {
                    try {
                        handleNotifyUser(job);
                        zeebePort.completeTask(job.getKey(), Map.of());
                    } catch (Exception e) {
                        LOG.severe("Error in NotifyUserWorker: " + e.getMessage());
                        zeebePort.failTask(job.getKey(), e.getMessage(), job.getRetries() - 1);
                    }
                })
                .open();

        LOG.info("NotifyUserWorker started successfully");
    }

    private void handleNotifyUser(ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();

        String userEmail = (String) variables.get("userEmail");
        String message = (String) variables.get("notificationMessage");
        String bookTitle = (String) variables.get("bookTitle");

        // Se não veio mensagem específica, monta uma padrão
        if (message == null || message.isEmpty()) {
            Boolean approved = (Boolean) variables.get("approved");
            String status = Boolean.TRUE.equals(approved) ? "approved" : "rejected";
            message = "Your loan request for book '" + bookTitle + "' has been " + status;
        }

        LOG.info("Sending notification to user: " + userEmail);
        LOG.info("Message: " + message);

        // TODO: Integrar com serviço de email real (Amazon SES, SendGrid, etc.)
        sendEmail(userEmail, "Loan Notification", message);

        LOG.info("Notification sent successfully to: " + userEmail);
    }

    private void sendEmail(String to, String subject, String body) {
        // Aqui você implementa o envio real de email
        // Exemplo com Amazon SES, SendGrid, Mailtrap, etc.

        // Por enquanto, apenas loga
        LOG.info("EMAIL TO: " + to);
        LOG.info("SUBJECT: " + subject);
        LOG.info("BODY: " + body);
    }

    @PreDestroy
    public void stopWorker() {
        LOG.info("Stopping NotifyUserWorker...");
        try {
            if (workerSubscription != null) {
                workerSubscription.close();
            }
        } catch (Exception e) {
            LOG.warning("Error stopping worker: " + e.getMessage());
        }
        LOG.info("NotifyUserWorker stopped");
    }
}