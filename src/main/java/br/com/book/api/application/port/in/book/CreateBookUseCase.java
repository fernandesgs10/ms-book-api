package br.com.book.api.application.port.in.book;

import br.com.book.api.domain.Book;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface CreateBookUseCase {

    Book execute(@Valid CreateBookCommand command);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateBookCommand {
        private String title;
        private String subtitle;
        private String isbn;
        private Integer publicationYear;
        private Integer numberOfPages;
        private BigDecimal price;
        private Integer edition;
        private String synopsis;
        private Integer stockQuantity;
        private Long publisherId;

        // 🔥 ADICIONAR ESTA LINHA 🔥
        private List<Long> authorIds = new ArrayList<>();  // IDs dos autores
    }
}