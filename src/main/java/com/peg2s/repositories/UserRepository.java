package com.peg2s.repositories;

import com.peg2s.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLoginIgnoreCase(String login);

    User findByEmail(String email);

    User deleteByLogin(String login);

}
