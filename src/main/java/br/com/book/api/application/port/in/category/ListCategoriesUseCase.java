package br.com.book.api.application.port.in.category;

import br.com.book.api.domain.Category;
import java.util.List;

public interface ListCategoriesUseCase {

    List<Category> findAll();

    List<Category> findByName(String name);
}