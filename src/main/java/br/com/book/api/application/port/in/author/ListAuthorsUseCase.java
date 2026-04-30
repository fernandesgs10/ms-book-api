package br.com.book.api.application.port.in.author;

import br.com.book.api.domain.Author;
import java.util.List;

public interface ListAuthorsUseCase {

    List<Author> execute();

    List<Author> execute(String name);

    List<Author> executeByNationality(String nationality);
}