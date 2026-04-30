package br.com.book.api.adapter.out.zeebe;

import br.com.book.api.application.port.out.ZeebePort;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

@ApplicationScoped
public class ZeebeAdapter implements ZeebePort {

    private static final Logger LOG = Logger.getLogger(ZeebeAdapter.class.getName());

    @Inject
    ZeebeClient zeebeClient;

    // =========================
    // Retry helper
    // =========================
    private <T> T executeWithRetry(Supplier<T> action) {
        int attempts = 0;
        int maxAttempts = 5;

        while (true) {
            try {
                return action.get();
            } catch (Exception e) {
                attempts++;
                LOG.warning("Zeebe not ready (attempt " + attempts + "): " + e.getMessage());

                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Zeebe unavailable after retries", e);
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    // =========================
    // Publish message
    // =========================
    @Override
    public void publishMessage(String messageName, String correlationId, Map<String, Object> variables) {
        LOG.info("Publishing message: " + messageName + " for correlationId: " + correlationId);

        executeWithRetry(() -> {
            zeebeClient.newPublishMessageCommand()
                    .messageName(messageName)
                    .correlationKey(correlationId)
                    .variables(variables)
                    .send()
                    .join();
            return null;
        });
    }

    // =========================
    // Start process
    // =========================
    @Override
    public long startProcess(String processId, Map<String, Object> variables) {
        LOG.info("Starting process: " + processId);

        ProcessInstanceEvent result = executeWithRetry(() ->
                zeebeClient.newCreateInstanceCommand()
                        .bpmnProcessId(processId)
                        .latestVersion()
                        .variables(variables)
                        .send()
                        .join()
        );

        LOG.info("Process started with key: " + result.getProcessInstanceKey());
        return result.getProcessInstanceKey();
    }

    // =========================
    // Complete task
    // =========================
    @Override
    public void completeTask(long jobKey, Map<String, Object> variables) {
        LOG.info("Completing task with jobKey: " + jobKey);

        executeWithRetry(() -> {
            zeebeClient.newCompleteCommand(jobKey)
                    .variables(variables)
                    .send()
                    .join();
            return null;
        });
    }

    // =========================
    // Fail task
    // =========================
    @Override
    public void failTask(long jobKey, String errorMessage, int retries) {
        LOG.warning("Failing task with jobKey: " + jobKey + " - " + errorMessage);

        executeWithRetry(() -> {
            zeebeClient.newFailCommand(jobKey)
                    .retries(retries)
                    .errorMessage(errorMessage)
                    .send()
                    .join();
            return null;
        });
    }
}