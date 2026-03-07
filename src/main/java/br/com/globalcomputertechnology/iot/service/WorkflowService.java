package br.com.globalcomputertechnology.iot.service;

import io.camunda.zeebe.client.ZeebeClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkflowService {

    private final ZeebeClient zeebeClient;

    public WorkflowService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @PostConstruct
    public void deploy() {

        try (var stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("bpmn/demo-process.bpmn")) {

            if (stream == null) {
                throw new RuntimeException("BPMN file not found in classpath");
            }

            zeebeClient.newDeployResourceCommand()
                    .addResourceStream(stream, "demo-process.bpmn")
                    .send()
                    .join();

            System.out.println("Process deployed!");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startProcess() {

        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("demo-process")
                .latestVersion()
                .send()
                .join();

        System.out.println("Process started!");
    }
}