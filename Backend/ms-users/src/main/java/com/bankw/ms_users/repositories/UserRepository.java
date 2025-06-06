package com.bankw.ms_users.repositories;

import com.bankw.ms_users.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);
}
