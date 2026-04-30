package br.com.book.api.application.port.in.category;

import br.com.book.api.domain.Category;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateCategoryUseCase {

    Category update(Long id, @Valid UpdateCategoryCommand command);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateCategoryCommand {
        private String name;
        private String description;
    }
}