package br.com.book.api.adapter.out.persistence.repository;

import br.com.book.api.adapter.out.persistence.entity.AppUserEntity;
import br.com.book.api.application.port.out.UserRepositoryPort;

import br.com.book.api.adapter.out.persistence.mapper.UserMapper;
import br.com.book.api.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryAdapter implements UserRepositoryPort, PanacheRepository<AppUserEntity> {

    @Inject
    UserMapper userMapper;

    @Override
    public List<User> findAllUsers() {
        return userMapper.toDomainList(listAll());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return findByIdOptional(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return find("email", email).firstResultOptional().map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findUserByCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional().map(userMapper::toDomain);
    }

    @Override
    public List<User> findUsersByName(String name) {
        return userMapper.toDomainList(
                find("name LIKE :name", Parameters.with("name", "%" + name + "%")).list()
        );
    }

    @Override
    public List<User> findActiveUsers() {
        return userMapper.toDomainList(find("isActive = true").list());
    }

    @Override
    public List<User> findInactiveUsers() {
        return userMapper.toDomainList(find("isActive = false").list());
    }

    @Override
    public List<User> findUsersRegisteredAfter(LocalDateTime date) {
        return userMapper.toDomainList(
                find("registrationDate > :date", Parameters.with("date", date)).list()
        );
    }

    @Override
    public List<User> findUsersWithActiveLoans() {
        return userMapper.toDomainList(
                find("SELECT DISTINCT u FROM UserEntity u JOIN u.loans l WHERE l.status = 'ACTIVE'").list()
        );
    }

    @Override
    public User save(User user) {
        AppUserEntity entity = userMapper.toEntity(user);
        persist(entity);
        return userMapper.toDomain(entity);
    }

    @Override
    public void deleteUserById(Long id) {
        deleteById(id);
    }

    @Override
    public boolean existsUserById(Long id) {
        return findByIdOptional(id).isPresent();
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return count("email", email) > 0;
    }

    @Override
    public boolean existsUserByCpf(String cpf) {
        return count("cpf", cpf) > 0;
    }
}