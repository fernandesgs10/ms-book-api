package br.com.book.api.adapter.out.persistence.repository;

import br.com.book.api.adapter.out.persistence.mapper.PublisherMapper;
import br.com.book.api.application.port.out.PublisherRepositoryPort;

import br.com.book.api.adapter.out.persistence.entity.PublisherEntity;

import br.com.book.api.domain.Publisher;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PublisherRepositoryAdapter implements PublisherRepositoryPort, PanacheRepository<PublisherEntity> {

    @Inject
    PublisherMapper publisherMapper;

    @Override
    public List<Publisher> findAllPublishers() {
        return publisherMapper.toDomainList(listAll());
    }

    @Override
    public Optional<Publisher> findPublisherById(Long id) {
        return findByIdOptional(id).map(publisherMapper::toDomain);
    }

    @Override
    public List<Publisher> findPublishersByName(String name) {
        return publisherMapper.toDomainList(
                find("name LIKE :name", Parameters.with("name", "%" + name + "%")).list()
        );
    }

    @Override
    public Optional<Publisher> findPublisherByCnpj(String cnpj) {
        return find("cnpj = :cnpj", Parameters.with("cnpj", cnpj))
                .firstResultOptional()
                .map(publisherMapper::toDomain);
    }

    @Override
    public List<Publisher> findPublishersByCity(String city) {
        return publisherMapper.toDomainList(
                find("city = :city", Parameters.with("city", city)).list()
        );
    }

    @Override
    public List<Publisher> findPublishersByState(String state) {
        return publisherMapper.toDomainList(
                find("state = :state", Parameters.with("state", state)).list()
        );
    }

    @Override
    public List<Publisher> findPublishersByCityAndState(String city, String state) {
        return publisherMapper.toDomainList(
                find("city = :city AND state = :state",
                        Parameters.with("city", city).and("state", state)).list()
        );
    }

    @Override
    public List<Publisher> findPublishersWithBooksPublishedInYear(Integer year) {
        return publisherMapper.toDomainList(
                find("SELECT DISTINCT p FROM PublisherEntity p JOIN p.books b WHERE b.publicationYear = :year",
                        Parameters.with("year", year)).list()
        );
    }

    @Override
    public Publisher save(Publisher publisher) {
        PublisherEntity entity = publisherMapper.toEntity(publisher);
        persist(entity);
        return publisherMapper.toDomain(entity);
    }

    @Override
    public void deletePublisherById(Long id) {
        deleteById(id);
    }

    @Override
    public boolean existsPublisherById(Long id) {
        return findByIdOptional(id).isPresent();
    }

    @Override
    public boolean existsPublisherByCnpj(String cnpj) {
        return count("cnpj = :cnpj", Parameters.with("cnpj", cnpj)) > 0;
    }
}