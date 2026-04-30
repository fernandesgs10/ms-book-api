package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.AuthorDTOMapper;
import br.com.book.api.application.port.in.author.*;
import br.com.book.api.domain.Author;
import br.com.book.api.gen.model.AuthorCreateRequest;
import br.com.book.api.gen.model.AuthorResponse;
import br.com.book.api.gen.model.AuthorUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorResourceTest {

    @Mock
    private CreateAuthorUseCase createAuthorUseCase;

    @Mock
    private GetAuthorUseCase getAuthorUseCase;

    @Mock
    private ListAuthorsUseCase listAuthorsUseCase;

    @Mock
    private UpdateAuthorUseCase updateAuthorUseCase;

    @Mock
    private DeleteAuthorUseCase deleteAuthorUseCase;

    @Mock
    private AuthorDTOMapper mapper;

    @InjectMocks
    private AuthorResource authorResource;

    private Author author;
    private AuthorResponse authorResponse;
    private AuthorCreateRequest createRequest;
    private AuthorUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("Machado de Assis");
        author.setNationality("Brasileiro");
        author.setBirthDate(LocalDate.of(1839, 6, 21));

        authorResponse = new AuthorResponse();
        authorResponse.setId(1L);
        authorResponse.setName("Machado de Assis");

        createRequest = new AuthorCreateRequest();
        createRequest.setName("Machado de Assis");
        createRequest.setNationality("Brasileiro");

        updateRequest = new AuthorUpdateRequest();
        updateRequest.setName("Machado de Assis - Edição Especial");
    }

    // ========== CREATE AUTHOR TESTS ==========

    @Test
    void createAuthor_ShouldReturnAuthorResponse_WhenSuccessful() {
        // Arrange
        CreateAuthorUseCase.CreateAuthorCommand command = mock(CreateAuthorUseCase.CreateAuthorCommand.class);
        when(mapper.toCreateCommand(createRequest)).thenReturn(command);
        when(createAuthorUseCase.execute(command)).thenReturn(author);
        when(mapper.toResponse(author)).thenReturn(authorResponse);

        // Act
        AuthorResponse response = authorResource.createAuthor(createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Machado de Assis", response.getName());
        verify(createAuthorUseCase).execute(command);
        verify(mapper).toResponse(author);
    }

    // ========== GET AUTHOR BY ID TESTS ==========

    @Test
    void getAuthorById_ShouldReturnAuthorResponse_WhenAuthorExists() {
        // Arrange
        when(getAuthorUseCase.findById(1L)).thenReturn(author);
        when(mapper.toResponse(author)).thenReturn(authorResponse);

        // Act
        AuthorResponse response = authorResource.getAuthorById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(getAuthorUseCase).findById(1L);
    }

    // ========== GET ALL AUTHORS TESTS ==========

    @Test
    void getAllAuthors_ShouldReturnListOfAuthors_WhenNoFilters() {
        // Arrange
        List<Author> authors = Arrays.asList(author);
        List<AuthorResponse> responses = Arrays.asList(authorResponse);
        when(listAuthorsUseCase.execute()).thenReturn(authors);
        when(mapper.toResponseList(authors)).thenReturn(responses);

        // Act
        List<AuthorResponse> result = authorResource.getAllAuthors(null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listAuthorsUseCase).execute();
        verify(listAuthorsUseCase, never()).execute(anyString());
        verify(listAuthorsUseCase, never()).executeByNationality(anyString());
    }

    @Test
    void getAllAuthors_ShouldReturnFilteredByName_WhenNameProvided() {
        // Arrange
        String name = "Machado";
        List<Author> authors = Arrays.asList(author);
        List<AuthorResponse> responses = Arrays.asList(authorResponse);
        when(listAuthorsUseCase.execute(name)).thenReturn(authors);
        when(mapper.toResponseList(authors)).thenReturn(responses);

        // Act
        List<AuthorResponse> result = authorResource.getAllAuthors(name, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listAuthorsUseCase).execute(name);
        verify(listAuthorsUseCase, never()).execute();
        verify(listAuthorsUseCase, never()).executeByNationality(anyString());
    }

    @Test
    void getAllAuthors_ShouldReturnFilteredByNationality_WhenNationalityProvided() {
        // Arrange
        String nationality = "Brasileiro";
        List<Author> authors = Arrays.asList(author);
        List<AuthorResponse> responses = Arrays.asList(authorResponse);
        when(listAuthorsUseCase.executeByNationality(nationality)).thenReturn(authors);
        when(mapper.toResponseList(authors)).thenReturn(responses);

        // Act
        List<AuthorResponse> result = authorResource.getAllAuthors(null, nationality);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listAuthorsUseCase).executeByNationality(nationality);
        verify(listAuthorsUseCase, never()).execute();
        verify(listAuthorsUseCase, never()).execute(anyString());
    }

    @Test
    void getAllAuthors_WithBlankName_ShouldReturnAllAuthors() {
        // Arrange
        List<Author> authors = Arrays.asList(author);
        List<AuthorResponse> responses = Arrays.asList(authorResponse);
        when(listAuthorsUseCase.execute()).thenReturn(authors);
        when(mapper.toResponseList(authors)).thenReturn(responses);

        // Act
        List<AuthorResponse> result = authorResource.getAllAuthors("", null);

        // Assert
        assertNotNull(result);
        verify(listAuthorsUseCase).execute();
        verify(listAuthorsUseCase, never()).execute(anyString());
    }

    @Test
    void getAllAuthors_WithBlankNationality_ShouldReturnAllAuthors() {
        // Arrange
        List<Author> authors = Arrays.asList(author);
        List<AuthorResponse> responses = Arrays.asList(authorResponse);
        when(listAuthorsUseCase.execute()).thenReturn(authors);
        when(mapper.toResponseList(authors)).thenReturn(responses);

        // Act
        List<AuthorResponse> result = authorResource.getAllAuthors(null, "");

        // Assert
        assertNotNull(result);
        verify(listAuthorsUseCase).execute();
        verify(listAuthorsUseCase, never()).executeByNationality(anyString());
    }

    @Test
    void getAllAuthors_WithBothFilters_ShouldPrioritizeName() {
        // Arrange
        String name = "Machado";
        String nationality = "Brasileiro";
        List<Author> authors = Arrays.asList(author);
        List<AuthorResponse> responses = Arrays.asList(authorResponse);
        when(listAuthorsUseCase.execute(name)).thenReturn(authors);
        when(mapper.toResponseList(authors)).thenReturn(responses);

        // Act
        List<AuthorResponse> result = authorResource.getAllAuthors(name, nationality);

        // Assert
        assertNotNull(result);
        // Name tem prioridade sobre nationality
        verify(listAuthorsUseCase).execute(name);
        verify(listAuthorsUseCase, never()).executeByNationality(anyString());
    }

    // ========== UPDATE AUTHOR TESTS ==========

    @Test
    void updateAuthor_ShouldReturnUpdatedAuthorResponse_WhenSuccessful() {
        // Arrange
        UpdateAuthorUseCase.UpdateAuthorCommand command = mock(UpdateAuthorUseCase.UpdateAuthorCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);
        when(updateAuthorUseCase.execute(1L, command)).thenReturn(author);
        when(mapper.toResponse(author)).thenReturn(authorResponse);

        // Act
        AuthorResponse response = authorResource.updateAuthor(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(updateAuthorUseCase).execute(1L, command);
    }

    // ========== DELETE AUTHOR TESTS ==========

    @Test
    void deleteAuthor_ShouldCallDeleteUseCase() {
        // Act
        authorResource.deleteAuthor(1L);

        // Assert
        verify(deleteAuthorUseCase).delete(1L);
    }

    // ========== EDGE CASES TESTS ==========

    @Test
    void getAuthorById_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        authorResource.getAuthorById(null);

        // Assert
        verify(getAuthorUseCase).findById(null);
    }

    @Test
    void deleteAuthor_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        authorResource.deleteAuthor(null);

        // Assert
        verify(deleteAuthorUseCase).delete(null);
    }
}