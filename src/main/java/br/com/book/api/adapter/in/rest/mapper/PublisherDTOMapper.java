package br.com.book.api.adapter.in.rest.mapper;

import br.com.book.api.domain.Publisher;
import br.com.book.api.gen.model.PublisherCreateRequest;
import br.com.book.api.gen.model.PublisherResponse;
import br.com.book.api.gen.model.PublisherUpdateRequest;
import br.com.book.api.application.port.in.publisher.CreatePublisherUseCase;
import br.com.book.api.application.port.in.publisher.UpdatePublisherUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PublisherDTOMapper {

    @Inject
    ModelMapper modelMapper;

    // Domain → Response
    public PublisherResponse toResponse(Publisher publisher) {
        return modelMapper.map(publisher, PublisherResponse.class);
    }

    // CreateRequest → Domain
    public Publisher toDomain(PublisherCreateRequest request) {
        return modelMapper.map(request, Publisher.class);
    }

    // CreateRequest → CreateCommand
    public CreatePublisherUseCase.CreatePublisherCommand toCreateCommand(PublisherCreateRequest request) {
        return modelMapper.map(request, CreatePublisherUseCase.CreatePublisherCommand.class);
    }

    // UpdateRequest → UpdateCommand
    public UpdatePublisherUseCase.UpdatePublisherCommand toUpdateCommand(PublisherUpdateRequest request) {
        return modelMapper.map(request, UpdatePublisherUseCase.UpdatePublisherCommand.class);
    }

    // Lista Domain → Lista Response
    public List<PublisherResponse> toResponseList(List<Publisher> publishers) {
        return publishers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}