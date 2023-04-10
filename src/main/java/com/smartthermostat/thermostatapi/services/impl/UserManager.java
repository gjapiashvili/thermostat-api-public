package com.smartthermostat.thermostatapi.services.impl;

import com.smartthermostat.thermostatapi.models.enums.Status;
import com.smartthermostat.thermostatapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Service
public class UserManager implements UserDetailsManager {
  private final UserRepository  userRepository;

  @Override
  public void createUser(UserDetails userDetails) {
  }

  @Override
  public void updateUser(UserDetails user) {

  }

  @Override
  public void deleteUser(String username) {

  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {

  }

  @Override
  public boolean userExists(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmailAndStatus(email, Status.ACTIVE)
        .orElseThrow(() -> new UsernameNotFoundException(
            MessageFormat.format("User with e-mail {0} not found", email)
        ));
  }

}
