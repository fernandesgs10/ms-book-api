package br.com.book.api.application.port.in.user;

import br.com.book.api.domain.User;
import java.util.List;

public interface ListUsersUseCase {

    List<User> findAll();

    List<User> findByName(String name);

    List<User> findActive();
}