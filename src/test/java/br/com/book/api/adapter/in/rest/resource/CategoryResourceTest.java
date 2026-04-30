package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.CategoryDTOMapper;
import br.com.book.api.application.port.in.category.*;
import br.com.book.api.domain.Category;
import br.com.book.api.gen.model.CategoryCreateRequest;
import br.com.book.api.gen.model.CategoryResponse;
import br.com.book.api.gen.model.CategoryUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryResourceTest {

    @Mock
    private CreateCategoryUseCase createCategoryUseCase;

    @Mock
    private GetCategoryUseCase getCategoryUseCase;

    @Mock
    private ListCategoriesUseCase listCategoriesUseCase;

    @Mock
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Mock
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Mock
    private CategoryDTOMapper mapper;

    @InjectMocks
    private CategoryResource categoryResource;

    private Category category;
    private CategoryResponse categoryResponse;
    private CategoryCreateRequest createRequest;
    private CategoryUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Literatura Brasileira");
        category.setDescription("Livros de autores brasileiros");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("Literatura Brasileira");

        createRequest = new CategoryCreateRequest();
        createRequest.setName("Literatura Brasileira");
        createRequest.setDescription("Livros de autores brasileiros");

        updateRequest = new CategoryUpdateRequest();
        updateRequest.setName("Literatura Brasileira - Clássicos");
        updateRequest.setDescription("Clássicos da literatura brasileira");
    }

    // ========== CREATE CATEGORY TESTS ==========

    @Test
    void createCategory_ShouldReturnCategoryResponse_WhenSuccessful() {
        // Arrange
        CreateCategoryUseCase.CreateCategoryCommand command = mock(CreateCategoryUseCase.CreateCategoryCommand.class);
        when(mapper.toCreateCommand(createRequest)).thenReturn(command);
        when(createCategoryUseCase.execute(command)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(categoryResponse);

        // Act
        CategoryResponse response = categoryResource.createCategory(createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Literatura Brasileira", response.getName());
        verify(createCategoryUseCase).execute(command);
        verify(mapper).toResponse(category);
    }

    // ========== GET CATEGORY BY ID TESTS ==========

    @Test
    void getCategoryById_ShouldReturnCategoryResponse_WhenCategoryExists() {
        // Arrange
        when(getCategoryUseCase.findById(1L)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(categoryResponse);

        // Act
        CategoryResponse response = categoryResource.getCategoryById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(getCategoryUseCase).findById(1L);
    }

    @Test
    void getCategoryById_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        categoryResource.getCategoryById(null);

        // Assert
        verify(getCategoryUseCase).findById(null);
    }

    // ========== GET ALL CATEGORIES TESTS ==========

    @Test
    void getAllCategories_ShouldReturnListOfCategories_WhenNoNameFilter() {
        // Arrange
        List<Category> categories = Arrays.asList(category);
        List<CategoryResponse> responses = Arrays.asList(categoryResponse);
        when(listCategoriesUseCase.findAll()).thenReturn(categories);
        when(mapper.toResponseList(categories)).thenReturn(responses);

        // Act
        List<CategoryResponse> result = categoryResource.getAllCategories(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listCategoriesUseCase).findAll();
        verify(listCategoriesUseCase, never()).findByName(anyString());
    }

    @Test
    void getAllCategories_ShouldReturnFilteredCategories_WhenNameProvided() {
        // Arrange
        String name = "Literatura";
        List<Category> categories = Arrays.asList(category);
        List<CategoryResponse> responses = Arrays.asList(categoryResponse);
        when(listCategoriesUseCase.findByName(name)).thenReturn(categories);
        when(mapper.toResponseList(categories)).thenReturn(responses);

        // Act
        List<CategoryResponse> result = categoryResource.getAllCategories(name);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listCategoriesUseCase).findByName(name);
        verify(listCategoriesUseCase, never()).findAll();
    }

    @Test
    void getAllCategories_WithBlankName_ShouldReturnAllCategories() {
        // Arrange
        List<Category> categories = Arrays.asList(category);
        List<CategoryResponse> responses = Arrays.asList(categoryResponse);
        when(listCategoriesUseCase.findAll()).thenReturn(categories);
        when(mapper.toResponseList(categories)).thenReturn(responses);

        // Act
        List<CategoryResponse> result = categoryResource.getAllCategories("");

        // Assert
        assertNotNull(result);
        verify(listCategoriesUseCase).findAll();
        verify(listCategoriesUseCase, never()).findByName(anyString());
    }

    @Test
    void getAllCategories_WithBlankNameWithSpaces_ShouldReturnAllCategories() {
        // Arrange
        List<Category> categories = Arrays.asList(category);
        List<CategoryResponse> responses = Arrays.asList(categoryResponse);
        when(listCategoriesUseCase.findAll()).thenReturn(categories);
        when(mapper.toResponseList(categories)).thenReturn(responses);

        // Act
        List<CategoryResponse> result = categoryResource.getAllCategories("   ");

        // Assert
        assertNotNull(result);
        verify(listCategoriesUseCase).findAll();
    }

    // ========== UPDATE CATEGORY TESTS ==========

    @Test
    void updateCategory_ShouldReturnUpdatedCategoryResponse_WhenSuccessful() {
        // Arrange
        UpdateCategoryUseCase.UpdateCategoryCommand command = mock(UpdateCategoryUseCase.UpdateCategoryCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);
        when(updateCategoryUseCase.update(1L, command)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(categoryResponse);

        // Act
        CategoryResponse response = categoryResource.updateCategory(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(updateCategoryUseCase).update(1L, command);
    }

    @Test
    void updateCategory_WithNullId_ShouldCallUseCaseWithNull() {
        // Arrange
        UpdateCategoryUseCase.UpdateCategoryCommand command = mock(UpdateCategoryUseCase.UpdateCategoryCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);

        // Act
        categoryResource.updateCategory(null, updateRequest);

        // Assert
        verify(updateCategoryUseCase).update(null, command);
    }

    // ========== DELETE CATEGORY TESTS ==========

    @Test
    void deleteCategory_ShouldCallDeleteUseCase() {
        // Act
        categoryResource.deleteCategory(1L);

        // Assert
        verify(deleteCategoryUseCase).delete(1L);
    }

    @Test
    void deleteCategory_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        categoryResource.deleteCategory(null);

        // Assert
        verify(deleteCategoryUseCase).delete(null);
    }
}