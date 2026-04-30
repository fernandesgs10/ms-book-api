package br.com.book.api.application.port.in.book;

import br.com.book.api.domain.Book;

public interface GetBookUseCase {

    Book findById(Long id);
}
