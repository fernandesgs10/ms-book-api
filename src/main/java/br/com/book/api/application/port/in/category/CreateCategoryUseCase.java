package br.com.book.api.application.port.in.category;

import br.com.book.api.domain.Category;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateCategoryUseCase {

    Category execute(@Valid CreateCategoryCommand command);


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateCategoryCommand {
        private String name;
        private String description;
    }
}