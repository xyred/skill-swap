package de.fherold.skillswap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.fherold.skillswap.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
