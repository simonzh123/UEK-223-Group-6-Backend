package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService, AbstractService<User> {
  User register(User user);

  User registerUser(User user);

  User getUserByMail(String email);

  boolean isUserAdmin(User user);

  User updateUserById(UUID id, User newUser);
}
