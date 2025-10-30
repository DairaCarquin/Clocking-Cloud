package com.clockingcloud.infrastructure.persistence;

import com.clockingcloud.domain.model.User;
import com.clockingcloud.domain.ports.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {
    Optional<User> findByEmail(String email);
}
