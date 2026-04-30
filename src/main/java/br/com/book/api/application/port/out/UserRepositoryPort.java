package br.com.book.api.application.port.out;


import br.com.book.api.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    // Buscas
    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByCpf(String cpf);

    List<User> findUsersByName(String name);

    List<User> findActiveUsers();

    List<User> findInactiveUsers();

    List<User> findUsersRegisteredAfter(java.time.LocalDateTime date);

    List<User> findUsersWithActiveLoans();

    // Operações
    User save(User user);

    void deleteUserById(Long id);

    // Validações
    boolean existsUserById(Long id);

    boolean existsUserByEmail(String email);

    boolean existsUserByCpf(String cpf);
}