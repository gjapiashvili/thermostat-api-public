package com.smartthermostat.thermostatapi.services.impl;

import com.smartthermostat.thermostatapi.config.security.TokenGenerator;
import com.smartthermostat.thermostatapi.exceptions.CustomException;
import com.smartthermostat.thermostatapi.models.dto.TokenDTO;
import com.smartthermostat.thermostatapi.models.dto.request.LoginRequestDto;
import com.smartthermostat.thermostatapi.models.dto.request.RefreshTokenRequestDto;
import com.smartthermostat.thermostatapi.models.dto.request.SignupRequestDto;
import com.smartthermostat.thermostatapi.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final String wrongCredentialsErrorMessage = "Invalid email address or password. Please double-check your login information and try again";
  @Autowired
  private DaoAuthenticationProvider daoAuthenticationProvider;
  @Autowired
  private TokenGenerator tokenGenerator;
  @Autowired
  private UserServiceImpl userService;
  @Autowired
  @Qualifier("jwtRefreshTokenAuthProvider")
  private JwtAuthenticationProvider refreshTokenAuthProvider;

  @Override
  public void signup(SignupRequestDto signupRequestDto) {
    var userFound = userService.findUserByEmail(signupRequestDto.getEmail());

    if (userFound.isPresent()) {
      throw new CustomException("User already exists", HttpStatus.CONFLICT);
    }

    userService.createUserInDatabaseAndSendEmailVerification(signupRequestDto);
  }

  @Override
  public TokenDTO login(LoginRequestDto loginRequest) {
    var user = userService.findUserByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new CustomException(wrongCredentialsErrorMessage, HttpStatus.UNAUTHORIZED));

    Authentication authentication;

    try {
      authentication = daoAuthenticationProvider.authenticate(
          UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getPassword()));
    } catch (BadCredentialsException ex) {
      throw new CustomException(wrongCredentialsErrorMessage, HttpStatus.UNAUTHORIZED);
    }

    //We could do this before calling authenticate() method
    //But we shouldn't give out this information before authenticating the user
    if (!user.getVerified()) {
      throw new CustomException("Your email address has not been verified yet", HttpStatus.UNAUTHORIZED);
    }

    return tokenGenerator.createToken(authentication);
  }

  @Override
  public boolean verifyEmail(String signupKey) {
    boolean ret = false;
    var userFound = userService.findUserBySignupKey(signupKey);

    if (userFound.isPresent()) {
      var user = userFound.get();
      if (!user.getVerified()) {
        user.setVerified(true);
        userService.saveUser(user);
      }
      ret = true;
    }

    return ret;
  }

  @Override
  public TokenDTO refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
    var authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(refreshTokenRequestDto.getRefreshToken()));
    return tokenGenerator.createToken(authentication);
  }
}
