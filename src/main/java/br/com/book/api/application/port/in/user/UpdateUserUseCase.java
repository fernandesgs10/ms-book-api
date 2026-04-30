package br.com.book.api.application.port.in.user;

import br.com.book.api.domain.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public interface UpdateUserUseCase {

    User update(Long id, @Valid UpdateUserCommand command);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateUserCommand {
        private String name;
        private String email;
        private String phone;
        private String address;
        private LocalDate birthDate;
    }
}