package br.com.book.api.application.port.in.author;


import br.com.book.api.domain.Author;

public interface GetAuthorUseCase {

    Author findById(Long id);
}