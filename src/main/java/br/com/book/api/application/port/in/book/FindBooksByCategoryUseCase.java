package br.com.book.api.application.port.in.book;

import br.com.book.api.domain.Book;
import java.util.List;

public interface FindBooksByCategoryUseCase {

    List<Book> execute(Long categoryId);

    List<Book> executeByCategoryName(String categoryName);
}