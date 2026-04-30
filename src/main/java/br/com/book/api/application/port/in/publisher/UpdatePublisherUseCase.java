package br.com.book.api.application.port.in.publisher;

import br.com.book.api.domain.Publisher;
import jakarta.validation.Valid;

public interface UpdatePublisherUseCase {

    Publisher update(Long id, @Valid UpdatePublisherCommand command);

    record UpdatePublisherCommand(
            String name,
            String cnpj,
            String city,
            String state,
            String website,
            String description
    ) {}
}