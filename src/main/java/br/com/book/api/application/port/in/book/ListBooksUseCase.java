package br.com.book.api.application.port.in.book;

import br.com.book.api.domain.Book;

import java.util.List;

public interface ListBooksUseCase {

    List<Book> findAll();

    List<Book> findByTitle(String title);

    List<Book> findAvailable();
}