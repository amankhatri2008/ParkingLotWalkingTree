package com.walkingtree.parkinglot.repository;

import com.walkingtree.parkinglot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     * This is used during the OAuth2 login process to check if a user already exists.
     *
     * @param email The user's email address.
     * @return An Optional containing the User if found, otherwise an empty Optional.
     */
    Optional<User> findByEmail(String email);
}