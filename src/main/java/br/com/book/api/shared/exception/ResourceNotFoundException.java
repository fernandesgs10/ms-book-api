package br.com.book.api.shared.exception;

public class ResourceNotFoundException extends RuntimeException {


    // Construtor 1: apenas mensagem
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Construtor 2: mensagem + causa
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Construtor 3: resourceName + id (mensagem formatada)
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id));
    }

    // Construtor 4: resourceName + field + value (mensagem formatada)
    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(String.format("%s not found with %s: %s", resourceName, field, value));
    }
}