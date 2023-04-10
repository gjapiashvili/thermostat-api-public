package com.smartthermostat.thermostatapi.services;

import com.smartthermostat.thermostatapi.models.dto.TokenDTO;
import com.smartthermostat.thermostatapi.models.dto.request.LoginRequestDto;
import com.smartthermostat.thermostatapi.models.dto.request.RefreshTokenRequestDto;
import com.smartthermostat.thermostatapi.models.dto.request.SignupRequestDto;

public interface AuthenticationService {
  void signup(SignupRequestDto request);
  TokenDTO login(LoginRequestDto request);
  boolean verifyEmail(String signupKey);

  TokenDTO refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
}
