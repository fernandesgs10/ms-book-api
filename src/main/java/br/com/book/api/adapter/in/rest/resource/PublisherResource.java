package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.PublisherDTOMapper;
import br.com.book.api.application.port.in.publisher.*;
import br.com.book.api.domain.Publisher;
import br.com.book.api.gen.api.PublishersApi;
import br.com.book.api.gen.model.PublisherCreateRequest;
import br.com.book.api.gen.model.PublisherResponse;
import br.com.book.api.gen.model.PublisherUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PublisherResource implements PublishersApi {

    @Inject
    CreatePublisherUseCase createPublisherUseCase;

    @Inject
    GetPublisherUseCase getPublisherUseCase;

    @Inject
    ListPublishersUseCase listPublishersUseCase;

    @Inject
    UpdatePublisherUseCase updatePublisherUseCase;

    @Inject
    DeletePublisherUseCase deletePublisherUseCase;

    @Inject
    PublisherDTOMapper mapper;

    @Override
    public List<PublisherResponse> getAllPublishers(String name, String city, String state) {
        if (name != null && !name.isBlank()) {
            return mapper.toResponseList(listPublishersUseCase.findByName(name));
        }
        if (city != null && !city.isBlank()) {
            return mapper.toResponseList(listPublishersUseCase.findByCity(city));
        }
        if (state != null && !state.isBlank()) {
            return mapper.toResponseList(listPublishersUseCase.findByState(state));
        }
        return mapper.toResponseList(listPublishersUseCase.findAll());
    }

    @Override
    public PublisherResponse getPublisherById(Long id) {
        Publisher publisher = getPublisherUseCase.findById(id);
        return mapper.toResponse(publisher);
    }

    @Override
    public PublisherResponse createPublisher(PublisherCreateRequest publisherCreateRequest) {
        CreatePublisherUseCase.CreatePublisherCommand command = mapper.toCreateCommand(publisherCreateRequest);
        Publisher publisher = createPublisherUseCase.execute(command);
        return mapper.toResponse(publisher);
    }

    @Override
    public PublisherResponse updatePublisher(Long id, PublisherUpdateRequest publisherUpdateRequest) {
        UpdatePublisherUseCase.UpdatePublisherCommand command = mapper.toUpdateCommand(publisherUpdateRequest);
        Publisher publisher = updatePublisherUseCase.update(id, command);
        return mapper.toResponse(publisher);
    }

    @Override
    public void deletePublisher(Long id) {
        deletePublisherUseCase.delete(id);
    }
}