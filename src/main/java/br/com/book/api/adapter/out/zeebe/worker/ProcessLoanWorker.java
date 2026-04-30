package br.com.book.api.adapter.out.zeebe.worker;

import br.com.book.api.application.port.in.loan.CreateLoanUseCase;
import io.camunda.zeebe.client.ZeebeClient;
import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
@Unremovable
@Startup
public class ProcessLoanWorker {

    private static final Logger LOG = Logger.getLogger(ProcessLoanWorker.class.getName());

    @Inject
    ZeebeClient zeebeClient;

    @Inject
    CreateLoanUseCase createLoanUseCase; // 🔥 usa interface

    @PostConstruct
    void init() {
        LOG.info("========== REGISTRANDO WORKER ==========");

        zeebeClient.newWorker()
                .jobType("process-loan")
                .handler((jobClient, job) -> {

                    LOG.info("🔥 Job recebido: " + job.getKey());

                    Map<String, Object> vars = job.getVariablesAsMap();

                    Long userId = Long.valueOf(vars.get("userId").toString());
                    Long bookId = Long.valueOf(vars.get("bookId").toString());

                    // 🔥 monta o command corretamente
                    CreateLoanUseCase.CreateLoanCommand command = new CreateLoanUseCase.CreateLoanCommand(
                            userId,
                            bookId,
                            LocalDateTime.now().plusDays(7), // prazo exemplo
                            "Loan created via Zeebe"
                    );

                    // 🔥 chama o service correto
                    createLoanUseCase.execute(command);

                    jobClient.newCompleteCommand(job.getKey())
                            .variables(Map.of("status", "completed"))
                            .send()
                            .join();

                    LOG.info("✅ Loan salvo e job completado!");
                })
                .name("process-loan-worker")
                .open();

        LOG.info("🚀 Worker registrado com sucesso!");
    }
}