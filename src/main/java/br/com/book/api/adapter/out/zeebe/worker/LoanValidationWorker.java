package br.com.book.api.adapter.out.zeebe.worker;

import br.com.book.api.application.port.out.ZeebePort;
import io.camunda.zeebe.client.ZeebeClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class LoanValidationWorker {

    private static final Logger LOG = Logger.getLogger(LoanValidationWorker.class.getName());

    @Inject
    ZeebeClient zeebeClient;

    @Inject
    ZeebePort zeebePort;

    private Thread workerThread;

    @PostConstruct
    public void startWorker() {
        workerThread = new Thread(() -> {
            zeebeClient.newWorker()
                    .jobType("validate-loan")
                    .handler((jobClient, job) -> {
                        Map<String, Object> variables = job.getVariablesAsMap();
                        LOG.info("Validating loan: " + variables);

                        // lógica de validação...

                        zeebePort.completeTask(job.getKey(), Map.of("loanValid", true));
                    })
                    .open();
        });
        workerThread.start();
    }

    @PreDestroy
    public void stopWorker() {
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }
}