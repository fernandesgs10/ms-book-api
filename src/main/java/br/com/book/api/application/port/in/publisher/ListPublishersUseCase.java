package br.com.book.api.application.port.in.publisher;

import br.com.book.api.domain.Publisher;
import java.util.List;

public interface ListPublishersUseCase {

    List<Publisher> findAll();

    List<Publisher> findByName(String name);

    List<Publisher> findByCity(String city);

    List<Publisher> findByState(String state);
}