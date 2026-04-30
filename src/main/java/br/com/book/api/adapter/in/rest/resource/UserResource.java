package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.UserDTOMapper;
import br.com.book.api.application.port.in.user.*;

import br.com.book.api.domain.User;
import br.com.book.api.gen.api.UsersApi;
import br.com.book.api.gen.model.UserCreateRequest;
import br.com.book.api.gen.model.UserResponse;
import br.com.book.api.gen.model.UserUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UserResource implements UsersApi {

    @Inject
    CreateUserUseCase createUserUseCase;

    @Inject
    GetUserUseCase getUserUseCase;

    @Inject
    ListUsersUseCase listUsersUseCase;

    @Inject
    UpdateUserUseCase updateUserUseCase;

    @Inject
    DeleteUserUseCase deleteUserUseCase;

    @Inject
    ActivateUserUseCase activateUserUseCase;

    @Inject
    DeactivateUserUseCase deactivateUserUseCase;

    @Inject
    UserDTOMapper mapper;

    @Override
    public void activateUser(Long id) {
        activateUserUseCase.activate(id);
    }

    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        CreateUserUseCase.CreateUserCommand command = mapper.toCreateCommand(userCreateRequest);
        User user = createUserUseCase.execute(command);
        return mapper.toResponse(user);
    }

    @Override
    public void deactivateUser(Long id) {
        deactivateUserUseCase.deactivate(id);
    }

    @Override
    public void deleteUser(Long id) {
        deleteUserUseCase.delete(id);
    }

    @Override
    public List<UserResponse> getAllUsers(String name, Boolean active) {
        if (name != null && !name.isBlank()) {
            return mapper.toResponseList(listUsersUseCase.findByName(name));
        }
        if (active != null && active) {
            return mapper.toResponseList(listUsersUseCase.findActive());
        }
        return mapper.toResponseList(listUsersUseCase.findAll());
    }

    @Override
    public UserResponse getUserByCpf(String cpf) {
        User user = getUserUseCase.findByCpf(cpf);
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = getUserUseCase.findByEmail(email);
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = getUserUseCase.findById(id);
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        UpdateUserUseCase.UpdateUserCommand command = mapper.toUpdateCommand(userUpdateRequest);
        User user = updateUserUseCase.update(id, command);
        return mapper.toResponse(user);
    }
}