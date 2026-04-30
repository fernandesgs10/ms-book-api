package br.com.book.api.application.service.impl;

import br.com.book.api.application.port.in.category.*;
import br.com.book.api.application.port.out.CategoryRepositoryPort;
import br.com.book.api.domain.Category;
import br.com.book.api.shared.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class CategoryServiceImpl implements
        CreateCategoryUseCase,
        GetCategoryUseCase,
        ListCategoriesUseCase,
        UpdateCategoryUseCase,
        DeleteCategoryUseCase {

    @Inject
    CategoryRepositoryPort categoryRepositoryPort;

    @Override
    public Category execute(CreateCategoryCommand command) {
        if (categoryRepositoryPort.existsCategoryByName(command.getName())) {
            throw new RuntimeException("Category already exists with name: " + command.getName());
        }

        Category category = new Category();
        category.setName(command.getName());
        category.setDescription(command.getDescription());

        return categoryRepositoryPort.save(category);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepositoryPort.findCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepositoryPort.findAllCategories();
    }

    @Override
    public List<Category> findByName(String name) {
        if (name == null || name.isBlank()) {
            return findAll();
        }
        return categoryRepositoryPort.findCategoriesByNameContaining(name);
    }

    @Override
    public Category update(Long id, UpdateCategoryCommand command) {
        Category category = categoryRepositoryPort.findCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        category.setName(command.getName());
        category.setDescription(command.getDescription());

        return categoryRepositoryPort.save(category);
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepositoryPort.existsCategoryById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }
        categoryRepositoryPort.deleteCategoryById(id);
    }
}