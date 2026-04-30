package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.PublisherDTOMapper;
import br.com.book.api.application.port.in.publisher.*;
import br.com.book.api.domain.Publisher;
import br.com.book.api.gen.model.PublisherCreateRequest;
import br.com.book.api.gen.model.PublisherResponse;
import br.com.book.api.gen.model.PublisherUpdateRequest;
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
class PublisherResourceTest {

    @Mock
    private CreatePublisherUseCase createPublisherUseCase;

    @Mock
    private GetPublisherUseCase getPublisherUseCase;

    @Mock
    private ListPublishersUseCase listPublishersUseCase;

    @Mock
    private UpdatePublisherUseCase updatePublisherUseCase;

    @Mock
    private DeletePublisherUseCase deletePublisherUseCase;

    @Mock
    private PublisherDTOMapper mapper;

    @InjectMocks
    private PublisherResource publisherResource;

    private Publisher publisher;
    private PublisherResponse publisherResponse;
    private PublisherCreateRequest createRequest;
    private PublisherUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Editora Globo");
        publisher.setCity("Rio de Janeiro");
        publisher.setState("RJ");
        publisher.setCnpj("12.345.678/0001-90");

        publisherResponse = new PublisherResponse();
        publisherResponse.setId(1L);
        publisherResponse.setName("Editora Globo");

        createRequest = new PublisherCreateRequest();
        createRequest.setName("Editora Globo");
        createRequest.setCity("Rio de Janeiro");
        createRequest.setState("RJ");

        updateRequest = new PublisherUpdateRequest();
        updateRequest.setName("Editora Globo - Digital");
        updateRequest.setCity("São Paulo");
        updateRequest.setState("SP");
    }

    // ========== CREATE PUBLISHER TESTS ==========

    @Test
    void createPublisher_ShouldReturnPublisherResponse_WhenSuccessful() {
        // Arrange
        CreatePublisherUseCase.CreatePublisherCommand command = mock(CreatePublisherUseCase.CreatePublisherCommand.class);
        when(mapper.toCreateCommand(createRequest)).thenReturn(command);
        when(createPublisherUseCase.execute(command)).thenReturn(publisher);
        when(mapper.toResponse(publisher)).thenReturn(publisherResponse);

        // Act
        PublisherResponse response = publisherResource.createPublisher(createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Editora Globo", response.getName());
        verify(createPublisherUseCase).execute(command);
        verify(mapper).toResponse(publisher);
    }

    // ========== GET PUBLISHER BY ID TESTS ==========

    @Test
    void getPublisherById_ShouldReturnPublisherResponse_WhenPublisherExists() {
        // Arrange
        when(getPublisherUseCase.findById(1L)).thenReturn(publisher);
        when(mapper.toResponse(publisher)).thenReturn(publisherResponse);

        // Act
        PublisherResponse response = publisherResource.getPublisherById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(getPublisherUseCase).findById(1L);
    }

    @Test
    void getPublisherById_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        publisherResource.getPublisherById(null);

        // Assert
        verify(getPublisherUseCase).findById(null);
    }

    // ========== GET ALL PUBLISHERS TESTS ==========

    @Test
    void getAllPublishers_ShouldReturnListOfPublishers_WhenNoFilters() {
        // Arrange
        List<Publisher> publishers = Arrays.asList(publisher);
        List<PublisherResponse> responses = Arrays.asList(publisherResponse);
        when(listPublishersUseCase.findAll()).thenReturn(publishers);
        when(mapper.toResponseList(publishers)).thenReturn(responses);

        // Act
        List<PublisherResponse> result = publisherResource.getAllPublishers(null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listPublishersUseCase).findAll();
        verify(listPublishersUseCase, never()).findByName(any());
        verify(listPublishersUseCase, never()).findByCity(any());
        verify(listPublishersUseCase, never()).findByState(any());
    }

    @Test
    void getAllPublishers_ShouldReturnFilteredByName_WhenNameProvided() {
        // Arrange
        String name = "Globo";
        List<Publisher> publishers = Arrays.asList(publisher);
        List<PublisherResponse> responses = Arrays.asList(publisherResponse);
        when(listPublishersUseCase.findByName(name)).thenReturn(publishers);
        when(mapper.toResponseList(publishers)).thenReturn(responses);

        // Act
        List<PublisherResponse> result = publisherResource.getAllPublishers(name, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listPublishersUseCase).findByName(name);
        verify(listPublishersUseCase, never()).findAll();
        verify(listPublishersUseCase, never()).findByCity(any());
        verify(listPublishersUseCase, never()).findByState(any());
    }

    @Test
    void getAllPublishers_ShouldReturnFilteredByCity_WhenCityProvided() {
        // Arrange
        String city = "Rio de Janeiro";
        List<Publisher> publishers = Arrays.asList(publisher);
        List<PublisherResponse> responses = Arrays.asList(publisherResponse);
        when(listPublishersUseCase.findByCity(city)).thenReturn(publishers);
        when(mapper.toResponseList(publishers)).thenReturn(responses);

        // Act
        List<PublisherResponse> result = publisherResource.getAllPublishers(null, city, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listPublishersUseCase).findByCity(city);
        verify(listPublishersUseCase, never()).findAll();
        verify(listPublishersUseCase, never()).findByName(any());
        verify(listPublishersUseCase, never()).findByState(any());
    }

    @Test
    void getAllPublishers_ShouldReturnFilteredByState_WhenStateProvided() {
        // Arrange
        String state = "RJ";
        List<Publisher> publishers = Arrays.asList(publisher);
        List<PublisherResponse> responses = Arrays.asList(publisherResponse);
        when(listPublishersUseCase.findByState(state)).thenReturn(publishers);
        when(mapper.toResponseList(publishers)).thenReturn(responses);

        // Act
        List<PublisherResponse> result = publisherResource.getAllPublishers(null, null, state);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listPublishersUseCase).findByState(state);
        verify(listPublishersUseCase, never()).findAll();
        verify(listPublishersUseCase, never()).findByName(any());
        verify(listPublishersUseCase, never()).findByCity(any());
    }

    @Test
    void getAllPublishers_WithNameAndCity_ShouldPrioritizeName() {
        // Arrange
        String name = "Globo";
        String city = "Rio de Janeiro";
        String state = "RJ";
        List<Publisher> publishers = Arrays.asList(publisher);
        List<PublisherResponse> responses = Arrays.asList(publisherResponse);
        when(listPublishersUseCase.findByName(name)).thenReturn(publishers);
        when(mapper.toResponseList(publishers)).thenReturn(responses);

        // Act
        List<PublisherResponse> result = publisherResource.getAllPublishers(name, city, state);

        // Assert
        assertNotNull(result);
        verify(listPublishersUseCase).findByName(name);
        verify(listPublishersUseCase, never()).findByCity(any());
        verify(listPublishersUseCase, never()).findByState(any());
    }

    @Test
    void getAllPublishers_WithCityAndState_ShouldPrioritizeCity() {
        // Arrange
        String city = "Rio de Janeiro";
        String state = "RJ";
        List<Publisher> publishers = Arrays.asList(publisher);
        List<PublisherResponse> responses = Arrays.asList(publisherResponse);
        when(listPublishersUseCase.findByCity(city)).thenReturn(publishers);
        when(mapper.toResponseList(publishers)).thenReturn(responses);

        // Act
        List<PublisherResponse> result = publisherResource.getAllPublishers(null, city, state);

        // Assert
        assertNotNull(result);
        verify(listPublishersUseCase).findByCity(city);
        verify(listPublishersUseCase, never()).findByState(any());
    }

    @Test
    void getAllPublishers_WithBlankName_ShouldReturnAll() {
        // Arrange
        List<Publisher> publishers = Arrays.asList(publisher);
        List<PublisherResponse> responses = Arrays.asList(publisherResponse);
        when(listPublishersUseCase.findAll()).thenReturn(publishers);
        when(mapper.toResponseList(publishers)).thenReturn(responses);

        // Act
        List<PublisherResponse> result = publisherResource.getAllPublishers("", null, null);

        // Assert
        assertNotNull(result);
        verify(listPublishersUseCase).findAll();
        verify(listPublishersUseCase, never()).findByName(anyString());
    }

    @Test
    void getAllPublishers_WithBlankCity_ShouldReturnAll() {
        // Arrange
        List<Publisher> publishers = Arrays.asList(publisher);
        List<PublisherResponse> responses = Arrays.asList(publisherResponse);
        when(listPublishersUseCase.findAll()).thenReturn(publishers);
        when(mapper.toResponseList(publishers)).thenReturn(responses);

        // Act
        List<PublisherResponse> result = publisherResource.getAllPublishers(null, "", null);

        // Assert
        assertNotNull(result);
        verify(listPublishersUseCase).findAll();
        verify(listPublishersUseCase, never()).findByCity(anyString());
    }

    @Test
    void getAllPublishers_WithBlankState_ShouldReturnAll() {
        // Arrange
        List<Publisher> publishers = Arrays.asList(publisher);
        List<PublisherResponse> responses = Arrays.asList(publisherResponse);
        when(listPublishersUseCase.findAll()).thenReturn(publishers);
        when(mapper.toResponseList(publishers)).thenReturn(responses);

        // Act
        List<PublisherResponse> result = publisherResource.getAllPublishers(null, null, "   ");

        // Assert
        assertNotNull(result);
        verify(listPublishersUseCase).findAll();
        verify(listPublishersUseCase, never()).findByState(anyString());
    }

    // ========== UPDATE PUBLISHER TESTS ==========

    @Test
    void updatePublisher_ShouldReturnUpdatedPublisherResponse_WhenSuccessful() {
        // Arrange
        UpdatePublisherUseCase.UpdatePublisherCommand command = mock(UpdatePublisherUseCase.UpdatePublisherCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);
        when(updatePublisherUseCase.update(1L, command)).thenReturn(publisher);
        when(mapper.toResponse(publisher)).thenReturn(publisherResponse);

        // Act
        PublisherResponse response = publisherResource.updatePublisher(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(updatePublisherUseCase).update(1L, command);
    }

    @Test
    void updatePublisher_WithNullId_ShouldCallUseCaseWithNull() {
        // Arrange
        UpdatePublisherUseCase.UpdatePublisherCommand command = mock(UpdatePublisherUseCase.UpdatePublisherCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);

        // Act
        publisherResource.updatePublisher(null, updateRequest);

        // Assert
        verify(updatePublisherUseCase).update(null, command);
    }

    // ========== DELETE PUBLISHER TESTS ==========

    @Test
    void deletePublisher_ShouldCallDeleteUseCase() {
        // Act
        publisherResource.deletePublisher(1L);

        // Assert
        verify(deletePublisherUseCase).delete(1L);
    }

    @Test
    void deletePublisher_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        publisherResource.deletePublisher(null);

        // Assert
        verify(deletePublisherUseCase).delete(null);
    }

    // ========== EDGE CASES TESTS ==========

    @Test
    void getPublisherById_ShouldHandleNonExistentId() {
        // Arrange
        when(getPublisherUseCase.findById(999L)).thenReturn(null);
        when(mapper.toResponse(null)).thenReturn(null);

        // Act
        PublisherResponse response = publisherResource.getPublisherById(999L);

        // Assert
        assertNull(response);
        verify(getPublisherUseCase).findById(999L);
    }
}