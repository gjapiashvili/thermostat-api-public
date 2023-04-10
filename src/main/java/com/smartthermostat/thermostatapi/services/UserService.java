package com.smartthermostat.thermostatapi.services;

import com.smartthermostat.thermostatapi.models.User;
import com.smartthermostat.thermostatapi.models.dto.request.SignupRequestDto;
import com.smartthermostat.thermostatapi.models.dto.request.UpdateProfileRequestDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> findUserByEmail(String email);

  Optional<User> findUserById(Long id);

  Optional<User> findUserById(String id);

  Optional<User> findUserBySignupKey(String signupKey);

  User saveUser(User user);

  List<User> findAllUsers();

  User getLoggedInUser();

  User updateLoggedInUserProfile(UpdateProfileRequestDto editProfileRequest);

  void deactivateLoggedInUserProfile();

  void createUserInDatabaseAndSendEmailVerification(SignupRequestDto signupRequestDto);

  User assignAdminRights(Long userId);

  User revokeAdminRights(Long userId);

}
