package br.com.book.api.application.port.out;

import br.com.book.api.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {

    // Buscas
    List<Category> findAllCategories();

    Optional<Category> findCategoryById(Long id);

    Optional<Category> findCategoryByName(String name);

    List<Category> findCategoriesByNameContaining(String name);

    List<Category> findCategoriesWithDescription();

    Optional<Category> findCategoryByIdWithBooks(Long id);

    // Operações
    Category save(Category category);

    void deleteCategoryById(Long id);

    // Validações
    boolean existsCategoryById(Long id);

    boolean existsCategoryByName(String name);
}