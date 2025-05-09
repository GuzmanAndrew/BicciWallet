package com.bankw.ms_users.services.impl;

import com.bankw.ms_users.config.JwtUtil;
import com.bankw.ms_users.entities.User;
import com.bankw.ms_users.exception.UserNotFoundException;
import com.bankw.ms_users.repositories.UserRepository;
import com.bankw.ms_users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public User register(User user) {
    user.setPassword(
        passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  public Optional<User> login(String username, String password) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UserNotFoundException("Usuario no existe");
    }
    if (passwordEncoder.matches(password, user.getPassword())) {
      return Optional.of(user);
    }
    return Optional.empty();
  }
}
