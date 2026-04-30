package br.com.book.api.application.port.out;

import java.util.Map;

public interface ZeebePort {

    void publishMessage(String messageName, String correlationId, Map<String, Object> variables);

    long startProcess(String processId, Map<String, Object> variables);

    void completeTask(long jobKey, Map<String, Object> variables);

    void failTask(long jobKey, String errorMessage, int retries);
}