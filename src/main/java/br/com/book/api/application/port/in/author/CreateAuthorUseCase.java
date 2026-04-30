package br.com.book.api.application.port.in.author;

import br.com.book.api.domain.Author;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public interface CreateAuthorUseCase {

    Author execute(@Valid CreateAuthorCommand command);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateAuthorCommand {
        private String name;
        private String nationality;
        private LocalDate birthDate;
        private String biography;
    }
}