package br.com.book.api.application.port.in.publisher;

import br.com.book.api.domain.Publisher;

public interface GetPublisherUseCase {

    Publisher findById(Long id);
}