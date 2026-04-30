package br.com.book.api.application.service.impl;

import br.com.book.api.application.port.in.publisher.*;
import br.com.book.api.application.port.out.PublisherRepositoryPort;
import br.com.book.api.domain.Publisher;
import br.com.book.api.shared.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class PublisherServiceImpl implements
        CreatePublisherUseCase,
        GetPublisherUseCase,
        ListPublishersUseCase,
        UpdatePublisherUseCase,
        DeletePublisherUseCase {

    @Inject
    PublisherRepositoryPort publisherRepositoryPort;

    @Override
    public Publisher execute(CreatePublisherCommand command) {
        if (command.getCnpj() != null && publisherRepositoryPort.existsPublisherByCnpj(command.getCnpj())) {  // ← corrigido
            throw new RuntimeException("Publisher already exists with CNPJ: " + command.getCnpj());
        }

        Publisher publisher = new Publisher();
        publisher.setName(command.getName());
        publisher.setCnpj(command.getCnpj());
        publisher.setCity(command.getCity());
        publisher.setState(command.getState());
        publisher.setWebsite(command.getWebsite());
        publisher.setDescription(command.getDescription());

        return publisherRepositoryPort.save(publisher);
    }

    @Override
    public Publisher findById(Long id) {
        return publisherRepositoryPort.findPublisherById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", id));
    }

    @Override
    public List<Publisher> findAll() {
        return publisherRepositoryPort.findAllPublishers();  // ← corrigido
    }

    @Override
    public List<Publisher> findByName(String name) {
        if (name == null || name.isBlank()) {
            return findAll();
        }
        return publisherRepositoryPort.findPublishersByName(name);  // ← corrigido
    }

    @Override
    public List<Publisher> findByCity(String city) {
        if (city == null || city.isBlank()) {
            return findAll();
        }
        return publisherRepositoryPort.findPublishersByCity(city);  // ← corrigido
    }

    @Override
    public List<Publisher> findByState(String state) {
        if (state == null || state.isBlank()) {
            return findAll();
        }
        return publisherRepositoryPort.findPublishersByState(state);  // ← corrigido
    }

    @Override
    public Publisher update(Long id, UpdatePublisherCommand command) {
        Publisher publisher = publisherRepositoryPort.findPublisherById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", id));

        publisher.setName(command.name());
        publisher.setCnpj(command.cnpj());
        publisher.setCity(command.city());
        publisher.setState(command.state());
        publisher.setWebsite(command.website());
        publisher.setDescription(command.description());

        return publisherRepositoryPort.save(publisher);
    }

    @Override
    public void delete(Long id) {
        if (!publisherRepositoryPort.existsPublisherById(id)) {  // ← corrigido
            throw new ResourceNotFoundException("Publisher", id);
        }
        publisherRepositoryPort.deletePublisherById(id);  // ← corrigido
    }
}