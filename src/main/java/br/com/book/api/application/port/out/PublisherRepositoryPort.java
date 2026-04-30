package br.com.book.api.application.port.out;

import br.com.book.api.domain.Publisher;

import java.util.List;
import java.util.Optional;

public interface PublisherRepositoryPort {

    // Buscas
    List<Publisher> findAllPublishers();

    Optional<Publisher> findPublisherById(Long id);

    List<Publisher> findPublishersByName(String name);

    Optional<Publisher> findPublisherByCnpj(String cnpj);

    List<Publisher> findPublishersByCity(String city);

    List<Publisher> findPublishersByState(String state);

    List<Publisher> findPublishersByCityAndState(String city, String state);

    List<Publisher> findPublishersWithBooksPublishedInYear(Integer year);

    // Operações
    Publisher save(Publisher publisher);

    void deletePublisherById(Long id);

    // Validações
    boolean existsPublisherById(Long id);

    boolean existsPublisherByCnpj(String cnpj);
}