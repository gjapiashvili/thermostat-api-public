package com.smartthermostat.thermostatapi.repositories;

import com.smartthermostat.thermostatapi.models.User;
import com.smartthermostat.thermostatapi.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  //Optional<User> findByEmail(String email);
  @Query("select u from User u where u.email = ?1 and u.status = ?2")
  Optional<User> findByEmailAndStatus(String email, Status status);
  Optional<User> findBySignupKey(String signUpKey);

  //boolean existsByUsername(String username);
  boolean existsByEmail(String email);

}