package com.example.login.repository;


import com.example.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find a user by username
    Optional<User> findByUsername(String username);

    // Find a user by email
    Optional<User> findByEmail(String email);


}
