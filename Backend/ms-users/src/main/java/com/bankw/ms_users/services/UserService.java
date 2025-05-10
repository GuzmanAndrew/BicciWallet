package com.bankw.ms_users.services;

import com.bankw.ms_users.model.dto.UpdateUserRequestDto;
import com.bankw.ms_users.model.dto.UserProfileDto;
import com.bankw.ms_users.model.entities.User;

import java.util.Map;
import java.util.Optional;

public interface UserService {

  User register(User user);

  Optional<User> login(String username, String password);

  Map<String, String> updateUser(UpdateUserRequestDto request);

  void deleteByUsername(String username);

  UserProfileDto getUserProfile(String username);
}
