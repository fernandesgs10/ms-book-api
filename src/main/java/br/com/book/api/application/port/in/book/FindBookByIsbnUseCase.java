package br.com.book.api.application.port.in.book;

import br.com.book.api.domain.Book;

public interface FindBookByIsbnUseCase {

    Book findByIsbn(String isbn);
}