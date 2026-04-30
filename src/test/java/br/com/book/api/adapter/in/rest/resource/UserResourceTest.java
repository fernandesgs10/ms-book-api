package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.UserDTOMapper;
import br.com.book.api.application.port.in.user.*;
import br.com.book.api.domain.User;
import br.com.book.api.gen.model.UserCreateRequest;
import br.com.book.api.gen.model.UserResponse;
import br.com.book.api.gen.model.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private GetUserUseCase getUserUseCase;

    @Mock
    private ListUsersUseCase listUsersUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private DeleteUserUseCase deleteUserUseCase;

    @Mock
    private ActivateUserUseCase activateUserUseCase;

    @Mock
    private DeactivateUserUseCase deactivateUserUseCase;

    @Mock
    private UserDTOMapper mapper;

    @InjectMocks
    private UserResource userResource;

    private User user;
    private UserResponse userResponse;
    private UserCreateRequest createRequest;
    private UserUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("João Silva");
        user.setEmail("joao@email.com");
        user.setCpf("123.456.789-00");
        user.setPhone("(11) 99999-9999");
        user.setAddress("Rua A, 123");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setActive(true);

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("João Silva");
        userResponse.setEmail("joao@email.com");

        createRequest = new UserCreateRequest();
        createRequest.setName("João Silva");
        createRequest.setEmail("joao@email.com");
        createRequest.setCpf("123.456.789-00");

        updateRequest = new UserUpdateRequest();
        updateRequest.setName("João Silva Atualizado");
        updateRequest.setPhone("(11) 88888-8888");
    }

    // ========== CREATE USER TESTS ==========

    @Test
    void createUser_ShouldReturnUserResponse_WhenSuccessful() {
        // Arrange
        CreateUserUseCase.CreateUserCommand command = mock(CreateUserUseCase.CreateUserCommand.class);
        when(mapper.toCreateCommand(createRequest)).thenReturn(command);
        when(createUserUseCase.execute(command)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse response = userResource.createUser(createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("João Silva", response.getName());
        assertEquals("joao@email.com", response.getEmail());
        verify(createUserUseCase).execute(command);
        verify(mapper).toResponse(user);
    }

    // ========== GET USER BY ID TESTS ==========

    @Test
    void getUserById_ShouldReturnUserResponse_WhenUserExists() {
        // Arrange
        when(getUserUseCase.findById(1L)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse response = userResource.getUserById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(getUserUseCase).findById(1L);
    }

    @Test
    void getUserById_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        userResource.getUserById(null);

        // Assert
        verify(getUserUseCase).findById(null);
    }

    // ========== GET USER BY EMAIL TESTS ==========

    @Test
    void getUserByEmail_ShouldReturnUserResponse_WhenUserExists() {
        // Arrange
        String email = "joao@email.com";
        when(getUserUseCase.findByEmail(email)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse response = userResource.getUserByEmail(email);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(getUserUseCase).findByEmail(email);
    }

    @Test
    void getUserByEmail_WithNullEmail_ShouldCallUseCaseWithNull() {
        // Act
        userResource.getUserByEmail(null);

        // Assert
        verify(getUserUseCase).findByEmail(null);
    }

    // ========== GET USER BY CPF TESTS ==========

    @Test
    void getUserByCpf_ShouldReturnUserResponse_WhenUserExists() {
        // Arrange
        String cpf = "123.456.789-00";
        when(getUserUseCase.findByCpf(cpf)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse response = userResource.getUserByCpf(cpf);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(getUserUseCase).findByCpf(cpf);
    }

    @Test
    void getUserByCpf_WithNullCpf_ShouldCallUseCaseWithNull() {
        // Act
        userResource.getUserByCpf(null);

        // Assert
        verify(getUserUseCase).findByCpf(null);
    }

    // ========== GET ALL USERS TESTS ==========

    @Test
    void getAllUsers_ShouldReturnListOfUsers_WhenNoFilters() {
        // Arrange
        List<User> users = Arrays.asList(user);
        List<UserResponse> responses = Arrays.asList(userResponse);
        when(listUsersUseCase.findAll()).thenReturn(users);
        when(mapper.toResponseList(users)).thenReturn(responses);

        // Act
        List<UserResponse> result = userResource.getAllUsers(null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listUsersUseCase).findAll();
        verify(listUsersUseCase, never()).findByName(any());
        verify(listUsersUseCase, never()).findActive();
    }

    @Test
    void getAllUsers_ShouldReturnFilteredByName_WhenNameProvided() {
        // Arrange
        String name = "João";
        List<User> users = Arrays.asList(user);
        List<UserResponse> responses = Arrays.asList(userResponse);
        when(listUsersUseCase.findByName(name)).thenReturn(users);
        when(mapper.toResponseList(users)).thenReturn(responses);

        // Act
        List<UserResponse> result = userResource.getAllUsers(name, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listUsersUseCase).findByName(name);
        verify(listUsersUseCase, never()).findAll();
        verify(listUsersUseCase, never()).findActive();
    }

    @Test
    void getAllUsers_ShouldReturnActiveUsers_WhenActiveFilterTrue() {
        // Arrange
        List<User> users = Arrays.asList(user);
        List<UserResponse> responses = Arrays.asList(userResponse);
        when(listUsersUseCase.findActive()).thenReturn(users);
        when(mapper.toResponseList(users)).thenReturn(responses);

        // Act
        List<UserResponse> result = userResource.getAllUsers(null, true);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listUsersUseCase).findActive();
        verify(listUsersUseCase, never()).findAll();
        verify(listUsersUseCase, never()).findByName(any());
    }

    @Test
    void getAllUsers_WithNameAndActiveFilter_ShouldPrioritizeName() {
        // Arrange
        String name = "João";
        List<User> users = Arrays.asList(user);
        List<UserResponse> responses = Arrays.asList(userResponse);
        when(listUsersUseCase.findByName(name)).thenReturn(users);
        when(mapper.toResponseList(users)).thenReturn(responses);

        // Act
        List<UserResponse> result = userResource.getAllUsers(name, true);

        // Assert
        assertNotNull(result);
        verify(listUsersUseCase).findByName(name);
        verify(listUsersUseCase, never()).findActive();
    }

    @Test
    void getAllUsers_WithBlankName_ShouldReturnAll() {
        // Arrange
        List<User> users = Arrays.asList(user);
        List<UserResponse> responses = Arrays.asList(userResponse);
        when(listUsersUseCase.findAll()).thenReturn(users);
        when(mapper.toResponseList(users)).thenReturn(responses);

        // Act
        List<UserResponse> result = userResource.getAllUsers("", null);

        // Assert
        assertNotNull(result);
        verify(listUsersUseCase).findAll();
        verify(listUsersUseCase, never()).findByName(anyString());
    }

    @Test
    void getAllUsers_WhenActiveIsFalse_ShouldReturnAll() {
        // Arrange
        List<User> users = Arrays.asList(user);
        List<UserResponse> responses = Arrays.asList(userResponse);
        when(listUsersUseCase.findAll()).thenReturn(users);
        when(mapper.toResponseList(users)).thenReturn(responses);

        // Act
        List<UserResponse> result = userResource.getAllUsers(null, false);

        // Assert
        assertNotNull(result);
        verify(listUsersUseCase).findAll();
        verify(listUsersUseCase, never()).findActive();
    }

    // ========== UPDATE USER TESTS ==========

    @Test
    void updateUser_ShouldReturnUpdatedUserResponse_WhenSuccessful() {
        // Arrange
        UpdateUserUseCase.UpdateUserCommand command = mock(UpdateUserUseCase.UpdateUserCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);
        when(updateUserUseCase.update(1L, command)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse response = userResource.updateUser(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(updateUserUseCase).update(1L, command);
    }

    @Test
    void updateUser_WithNullId_ShouldCallUseCaseWithNull() {
        // Arrange
        UpdateUserUseCase.UpdateUserCommand command = mock(UpdateUserUseCase.UpdateUserCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);

        // Act
        userResource.updateUser(null, updateRequest);

        // Assert
        verify(updateUserUseCase).update(null, command);
    }

    // ========== DELETE USER TESTS ==========

    @Test
    void deleteUser_ShouldCallDeleteUseCase() {
        // Act
        userResource.deleteUser(1L);

        // Assert
        verify(deleteUserUseCase).delete(1L);
    }

    @Test
    void deleteUser_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        userResource.deleteUser(null);

        // Assert
        verify(deleteUserUseCase).delete(null);
    }

    // ========== ACTIVATE USER TESTS ==========

    @Test
    void activateUser_ShouldCallActivateUseCase() {
        // Act
        userResource.activateUser(1L);

        // Assert
        verify(activateUserUseCase).activate(1L);
    }

    @Test
    void activateUser_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        userResource.activateUser(null);

        // Assert
        verify(activateUserUseCase).activate(null);
    }

    // ========== DEACTIVATE USER TESTS ==========

    @Test
    void deactivateUser_ShouldCallDeactivateUseCase() {
        // Act
        userResource.deactivateUser(1L);

        // Assert
        verify(deactivateUserUseCase).deactivate(1L);
    }

    @Test
    void deactivateUser_WithNullId_ShouldCallUseCaseWithNull() {
        // Act
        userResource.deactivateUser(null);

        // Assert
        verify(deactivateUserUseCase).deactivate(null);
    }

    // ========== EDGE CASES TESTS ==========

    @Test
    void getUserById_ShouldHandleNonExistentId() {
        // Arrange
        when(getUserUseCase.findById(999L)).thenReturn(null);
        when(mapper.toResponse(null)).thenReturn(null);

        // Act
        UserResponse response = userResource.getUserById(999L);

        // Assert
        assertNull(response);
        verify(getUserUseCase).findById(999L);
    }

    @Test
    void getUserByEmail_ShouldHandleNonExistentEmail() {
        // Arrange
        String email = "naoexiste@email.com";
        when(getUserUseCase.findByEmail(email)).thenReturn(null);
        when(mapper.toResponse(null)).thenReturn(null);

        // Act
        UserResponse response = userResource.getUserByEmail(email);

        // Assert
        assertNull(response);
        verify(getUserUseCase).findByEmail(email);
    }

    @Test
    void getUserByCpf_ShouldHandleNonExistentCpf() {
        // Arrange
        String cpf = "000.000.000-00";
        when(getUserUseCase.findByCpf(cpf)).thenReturn(null);
        when(mapper.toResponse(null)).thenReturn(null);

        // Act
        UserResponse response = userResource.getUserByCpf(cpf);

        // Assert
        assertNull(response);
        verify(getUserUseCase).findByCpf(cpf);
    }
}