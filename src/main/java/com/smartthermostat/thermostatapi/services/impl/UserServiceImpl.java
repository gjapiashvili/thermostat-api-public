package com.smartthermostat.thermostatapi.services.impl;

import com.smartthermostat.thermostatapi.exceptions.CustomException;
import com.smartthermostat.thermostatapi.models.User;
import com.smartthermostat.thermostatapi.models.dto.request.SignupRequestDto;
import com.smartthermostat.thermostatapi.models.dto.request.UpdateProfileRequestDto;
import com.smartthermostat.thermostatapi.models.enums.Authority;
import com.smartthermostat.thermostatapi.models.enums.Status;
import com.smartthermostat.thermostatapi.repositories.UserRepository;
import com.smartthermostat.thermostatapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final NotificationServiceImpl notificationService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmailAndStatus(email, Status.ACTIVE);
  }

  @Override
  public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> findUserById(String id) {
    long userId;

    try {
      userId = Long.parseLong(id);
    } catch (Exception e) {
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while trying to search user by string Id");
    }

    return userRepository.findById(userId);
  }

  @Override
  public Optional<User> findUserBySignupKey(String signupKey) {
    return userRepository.findBySignupKey(signupKey);
  }

  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getLoggedInUser() {
    var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!(user instanceof User)) {
      throw new CustomException(HttpStatus.UNAUTHORIZED, "Error while trying to get current logged in user");
    }

    return (User)user;
  }

  @Override
  public User updateLoggedInUserProfile(UpdateProfileRequestDto editProfileRequest) {
    var user = getLoggedInUser();

    user.setFirstname(editProfileRequest.getFirstName());
    user.setLastname(editProfileRequest.getLastName());
    user.setReceiveAlerts(editProfileRequest.isReceiveAlerts());
    saveUser(user);
    return user;
  }

  @Override
  public void deactivateLoggedInUserProfile() {
    var user = getLoggedInUser();

    user.setStatus(Status.INACTIVE);
    saveUser(user);
  }

  @Override
  public void createUserInDatabaseAndSendEmailVerification(SignupRequestDto signupRequestDto) {
    User user = new User();
    user.setFirstname(signupRequestDto.getFirstname());
    user.setLastname(signupRequestDto.getLastname());
    user.setEmail(signupRequestDto.getEmail());
    user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
    user.setSignupKey(UUID.randomUUID().toString());
    user.setAuthorities(Set.of(Authority.USER));
    user.setVerified(false);
    user.setReceiveAlerts(true); //By default, the alert is on. However, users can turn it off using update-profile
    user.setStatus(Status.ACTIVE);
    user.setCreatedAt(new Date());
    saveUser(user);

    notificationService.sendMailVerificationEmail(user);
  }

  @Override
  public User assignAdminRights(Long userId) {
    User user = null;
    var userOptional = findUserById(userId);

    if (userOptional.isPresent()) {
      user = userOptional.get();
      user.addAuthority(Authority.ADMIN);
      userRepository.save(user);
    }

    return user;
  }

  @Override
  public User revokeAdminRights(Long userId) {
    User user = null;
    var userOptional = findUserById(userId);

    if (userOptional.isPresent()) {
      user = userOptional.get();
      user.removeAuthority(Authority.ADMIN);
      userRepository.save(user);
    }

    return user;
  }

}


