package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.CategoryDTOMapper;
import br.com.book.api.application.port.in.category.*;
import br.com.book.api.domain.Category;
import br.com.book.api.gen.api.CategoriesApi;
import br.com.book.api.gen.model.CategoryCreateRequest;
import br.com.book.api.gen.model.CategoryResponse;
import br.com.book.api.gen.model.CategoryUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CategoryResource implements CategoriesApi {

    @Inject
    CreateCategoryUseCase createCategoryUseCase;

    @Inject
    GetCategoryUseCase getCategoryUseCase;

    @Inject
    ListCategoriesUseCase listCategoriesUseCase;

    @Inject
    UpdateCategoryUseCase updateCategoryUseCase;

    @Inject
    DeleteCategoryUseCase deleteCategoryUseCase;

    @Inject
    CategoryDTOMapper mapper;

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        CreateCategoryUseCase.CreateCategoryCommand command = mapper.toCreateCommand(categoryCreateRequest);
        Category category = createCategoryUseCase.execute(command);
        return mapper.toResponse(category);
    }

    @Override
    public void deleteCategory(Long id) {
        deleteCategoryUseCase.delete(id);
    }

    @Override
    public List<CategoryResponse> getAllCategories(String name) {
        if (name != null && !name.isBlank()) {
            return mapper.toResponseList(listCategoriesUseCase.findByName(name));
        }
        return mapper.toResponseList(listCategoriesUseCase.findAll());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = getCategoryUseCase.findById(id);
        return mapper.toResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        UpdateCategoryUseCase.UpdateCategoryCommand command = mapper.toUpdateCommand(categoryUpdateRequest);
        Category category = updateCategoryUseCase.update(id, command);
        return mapper.toResponse(category);
    }
}