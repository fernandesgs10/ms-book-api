package br.com.book.api.config;

import br.com.book.api.adapter.out.zeebe.worker.ProcessLoanWorker;
import io.camunda.zeebe.client.ZeebeClient;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.util.logging.Logger;

@ApplicationScoped
public class ZeebeDeployer {

    private static final Logger LOG = Logger.getLogger(ZeebeDeployer.class.getName());

    @Inject
    ZeebeClient zeebeClient;

    @Inject
    ProcessLoanWorker processLoanWorker; // força inicialização

    void onStart(@Observes StartupEvent event) {
        LOG.info("🚀 ZeebeDeployer - Application starting...");

        deployBpmn();

        LOG.info("✅ ZeebeDeployer - Done! Worker should be active.");
    }

    private void deployBpmn() {
        try {
            InputStream bpmnStream = getClass().getClassLoader()
                    .getResourceAsStream("processes/book-loan-process.bpmn");

            if (bpmnStream == null) {
                LOG.severe("❌ BPMN not found in classpath!");
                return;
            }

            zeebeClient.newDeployResourceCommand()
                    .addResourceStream(bpmnStream, "book-loan-process.bpmn")
                    .send()
                    .join();

            LOG.info("✅ Process 'book-loan-process' deployed to Zeebe!");

        } catch (Exception e) {
            LOG.severe("❌ Failed to deploy BPMN: " + e.getMessage());
        }
    }
}