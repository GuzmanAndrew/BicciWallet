package com.bankw.ms_users.services;

import com.bankw.ms_users.entities.User;

import java.util.Map;
import java.util.Optional;

public interface UserService {

    User register(User user);
    Optional<User> login(String username, String password);
    Map<String, String> updateUserRole(String username, String newRole);
    void deleteByUsername(String username);

}
