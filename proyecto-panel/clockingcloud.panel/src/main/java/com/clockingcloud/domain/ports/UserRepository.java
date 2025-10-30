package com.clockingcloud.domain.ports;

import java.util.Optional;

import com.clockingcloud.domain.model.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}