package com.bankw.ms_users.services.impl;

import com.bankw.ms_users.model.dto.UpdateUserRequestDto;
import com.bankw.ms_users.model.dto.UserProfileDto;
import com.bankw.ms_users.model.entities.User;
import com.bankw.ms_users.exception.UserNotFoundException;
import com.bankw.ms_users.repositories.UserRepository;
import com.bankw.ms_users.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

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
  public Map<String, String> updateUser(UpdateUserRequestDto request) {
    if (request.getUsername() == null || request.getUsername().isBlank()) {
      throw new IllegalArgumentException("El campo 'username' es obligatorio.");
    }

    Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException("Usuario no encontrado");
    }

    User user = optionalUser.get();

    if (request.getName() != null && !request.getName().isBlank()) {
      user.setName(request.getName());
    }

    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    if (request.getRole() != null && !request.getRole().isBlank()) {
      user.setRole(request.getRole());
    }

    if (request.getEmail() != null && !request.getEmail().isBlank()) {
      user.setEmail(request.getEmail());
    }

    userRepository.save(user);

    Map<String, String> response = new HashMap<>();
    response.put("message", "Usuario actualizado exitosamente");
    response.put("username", user.getUsername());

    return response;
  }

  @Override
  @Transactional
  public void deleteByUsername(String username) {
    userRepository.deleteByUsername(username);
  }

  @Override
  public UserProfileDto getUserProfile(String username) {
    Optional<User> optionalUser = userRepository.findByUsername(username);
    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException("Usuario no encontrado");
    }

    User user = optionalUser.get();
    return new UserProfileDto(
            user.getName(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
    );
  }
}
