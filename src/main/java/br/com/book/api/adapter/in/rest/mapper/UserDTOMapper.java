package br.com.book.api.adapter.in.rest.mapper;

import br.com.book.api.domain.User;
import br.com.book.api.gen.model.UserCreateRequest;
import br.com.book.api.gen.model.UserResponse;
import br.com.book.api.gen.model.UserUpdateRequest;
import br.com.book.api.application.port.in.user.CreateUserUseCase;
import br.com.book.api.application.port.in.user.UpdateUserUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserDTOMapper {

    @Inject
    ModelMapper modelMapper;

    // Domain → Response
    public UserResponse toResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    // CreateRequest → Domain
    public User toDomain(UserCreateRequest request) {
        return modelMapper.map(request, User.class);
    }

    // CreateRequest → CreateCommand
    public CreateUserUseCase.CreateUserCommand toCreateCommand(UserCreateRequest request) {
        return modelMapper.map(request, CreateUserUseCase.CreateUserCommand.class);
    }

    // UpdateRequest → UpdateCommand
    public UpdateUserUseCase.UpdateUserCommand toUpdateCommand(UserUpdateRequest request) {
        return modelMapper.map(request, UpdateUserUseCase.UpdateUserCommand.class);
    }

    // Lista Domain → Lista Response
    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}