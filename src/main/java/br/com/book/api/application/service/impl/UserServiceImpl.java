package br.com.book.api.application.service.impl;

import br.com.book.api.application.port.in.user.*;
import br.com.book.api.application.port.out.UserRepositoryPort;
import br.com.book.api.domain.User;
import br.com.book.api.shared.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class UserServiceImpl implements
        CreateUserUseCase,
        GetUserUseCase,
        ListUsersUseCase,
        UpdateUserUseCase,
        DeleteUserUseCase,
        ActivateUserUseCase,
        DeactivateUserUseCase {

    @Inject
    UserRepositoryPort userRepositoryPort;

    @Override
    public User execute(CreateUserCommand command) {
        if (userRepositoryPort.existsUserByEmail(command.getEmail())) {  // ← corrigido
            throw new RuntimeException("Email already exists: " + command.getEmail());
        }

        if (userRepositoryPort.existsUserByCpf(command.getCpf())) {  // ← corrigido
            throw new RuntimeException("CPF already exists: " + command.getCpf());
        }

        User user = new User();
        user.setName(command.getName());
        user.setEmail(command.getEmail());
        user.setCpf(command.getCpf());
        user.setPhone(command.getPhone());
        user.setAddress(command.getAddress());
        user.setBirthDate(command.getBirthDate());

        return userRepositoryPort.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepositoryPort.findUserById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepositoryPort.findUserByEmail(email)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public User findByCpf(String cpf) {
        return userRepositoryPort.findUserByCpf(cpf)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("User", "cpf", cpf));
    }

    @Override
    public List<User> findAll() {
        return userRepositoryPort.findAllUsers();  // ← corrigido
    }

    @Override
    public List<User> findByName(String name) {
        if (name == null || name.isBlank()) {
            return findAll();
        }
        return userRepositoryPort.findUsersByName(name);  // ← corrigido
    }

    @Override
    public List<User> findActive() {
        return userRepositoryPort.findActiveUsers();  // ← corrigido
    }

    @Override
    public User update(Long id, UpdateUserCommand command) {
        User user = userRepositoryPort.findUserById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        user.setName(command.getName());
        user.setEmail(command.getEmail());
        user.setPhone(command.getPhone());
        user.setAddress(command.getAddress());
        user.setBirthDate(command.getBirthDate());

        return userRepositoryPort.save(user);
    }

    @Override
    public void delete(Long id) {
        if (!userRepositoryPort.existsUserById(id)) {  // ← corrigido
            throw new ResourceNotFoundException("User", id);
        }
        userRepositoryPort.deleteUserById(id);  // ← corrigido
    }

    @Override
    public void activate(Long id) {
        User user = userRepositoryPort.findUserById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.activate();
        userRepositoryPort.save(user);
    }

    @Override
    public void deactivate(Long id) {
        User user = userRepositoryPort.findUserById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.deactivate();
        userRepositoryPort.save(user);
    }
}