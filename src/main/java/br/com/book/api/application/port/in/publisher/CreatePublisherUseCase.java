package br.com.book.api.application.port.in.publisher;

import br.com.book.api.domain.Publisher;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreatePublisherUseCase {

    Publisher execute(@Valid CreatePublisherCommand command);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreatePublisherCommand {
        private String name;
        private String cnpj;
        private String city;
        private String state;
        private String website;
        private String description;
    }
}