package com.bankw.ms_users.services;

import com.bankw.ms_users.entities.User;

import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> login(String username, String password);
}
