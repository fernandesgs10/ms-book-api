package br.com.book.api.application.port.in.user;

import br.com.book.api.domain.User;

public interface GetUserUseCase {

    User findById(Long id);

    User findByEmail(String email);

    User findByCpf(String cpf);
}