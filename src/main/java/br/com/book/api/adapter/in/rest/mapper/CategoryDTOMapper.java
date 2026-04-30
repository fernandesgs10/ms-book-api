package br.com.book.api.adapter.in.rest.mapper;

import br.com.book.api.domain.Category;
import br.com.book.api.gen.model.CategoryCreateRequest;
import br.com.book.api.gen.model.CategoryResponse;
import br.com.book.api.gen.model.CategoryUpdateRequest;
import br.com.book.api.application.port.in.category.CreateCategoryUseCase;
import br.com.book.api.application.port.in.category.UpdateCategoryUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryDTOMapper {

    @Inject
    ModelMapper modelMapper;

    // Domain → Response
    public CategoryResponse toResponse(Category category) {
        return modelMapper.map(category, CategoryResponse.class);
    }

    // CreateRequest → Domain
    public Category toDomain(CategoryCreateRequest request) {
        return modelMapper.map(request, Category.class);
    }

    // CreateRequest → CreateCommand
    public CreateCategoryUseCase.CreateCategoryCommand toCreateCommand(CategoryCreateRequest request) {
        return modelMapper.map(request, CreateCategoryUseCase.CreateCategoryCommand.class);
    }

    // UpdateRequest → UpdateCommand
    public UpdateCategoryUseCase.UpdateCategoryCommand toUpdateCommand(CategoryUpdateRequest request) {
        return modelMapper.map(request, UpdateCategoryUseCase.UpdateCategoryCommand.class);
    }

    // Lista Domain → Lista Response
    public List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}