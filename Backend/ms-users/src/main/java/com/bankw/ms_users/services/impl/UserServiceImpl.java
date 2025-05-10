package com.bankw.ms_users.services.impl;

import com.bankw.ms_users.entities.User;
import com.bankw.ms_users.exception.UserNotFoundException;
import com.bankw.ms_users.repositories.UserRepository;
import com.bankw.ms_users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User register(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  public Optional<User> login(String username, String password) {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UserNotFoundException("Usuario no existe");
    }
    if (passwordEncoder.matches(password, user.get().getPassword())) {
      return user;
    }
    return Optional.empty();
  }

  @Override
  public Map<String, String> updateUserRole(String username, String newRole) {
    Optional<User> optionalUser = userRepository.findByUsername(username);
    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException("Usuario no encontrado");
    }

    User user = optionalUser.get();
    user.setRole(newRole);
    userRepository.save(user);

    Map<String, String> response = new HashMap<>();
    response.put("message", "Rol actualizado exitosamente");
    response.put("username", username);
    response.put("newRole", newRole);

    return response;
  }

  @Override
  public void deleteByUsername(String username) {
    userRepository.deleteByUsername(username);
  }
}
