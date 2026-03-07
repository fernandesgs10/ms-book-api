package br.com.globalcomputertechnology.iot.worker;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;

import io.quarkus.runtime.StartupEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
public class EmailWorker {

    private final ZeebeClient zeebeClient;

    public EmailWorker(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
        System.out.println("📦 EmailWorker bean created");
    }

    void onStart(@Observes StartupEvent ev) {

        System.out.println("🚀 Starting Email Worker...");

        zeebeClient.newWorker()
                .jobType("send-email")
                .handler(this::handleJob)
                .name("email-worker")
                .timeout(Duration.ofSeconds(30))
                .maxJobsActive(10)
                .open();
    }

    private void handleJob(JobClient client, ActivatedJob job) {

        System.out.println("📧 Processing email task");

        Map<String, Object> variables = job.getVariablesAsMap();
        System.out.println("Variables: " + variables);

        client.newCompleteCommand(job.getKey())
                .send()
                .join();

        System.out.println("✅ Job completed");
    }
}