package br.com.book.api.application.port.in.category;

import br.com.book.api.domain.Category;

public interface GetCategoryUseCase {

    Category findById(Long id);
}