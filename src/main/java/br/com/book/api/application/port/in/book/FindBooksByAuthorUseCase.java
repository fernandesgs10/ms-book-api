package br.com.book.api.application.port.in.book;

import br.com.book.api.domain.Book;
import java.util.List;

public interface FindBooksByAuthorUseCase {

    List<Book> execute(Long authorId);

    List<Book> executeByAuthorName(String authorName);
}