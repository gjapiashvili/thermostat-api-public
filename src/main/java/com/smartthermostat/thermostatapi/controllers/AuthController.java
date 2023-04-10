package com.smartthermostat.thermostatapi.controllers;

import com.smartthermostat.thermostatapi.controllers.config.ResponseWrapper;
import com.smartthermostat.thermostatapi.models.dto.request.LoginRequestDto;
import com.smartthermostat.thermostatapi.models.dto.request.RefreshTokenRequestDto;
import com.smartthermostat.thermostatapi.models.dto.request.SignupRequestDto;
import com.smartthermostat.thermostatapi.services.impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.smartthermostat.thermostatapi.controllers.config.ControllerBase.*;

@RestController
@RequestMapping(path = ENTRY_POINT_V1 + AUTH_ENTRY_POINT)
public class AuthController {
  @Autowired
  private AuthenticationServiceImpl authenticationService;

  @PostMapping("/signup")
  public ResponseEntity<ResponseWrapper> signup(@RequestBody SignupRequestDto signupRequest) {
    authenticationService.signup(signupRequest);

    var responseWrapper = new ResponseWrapper(HttpStatus.CREATED);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseWrapper> login(@RequestBody LoginRequestDto loginRequest) {
    var result = authenticationService.login(loginRequest);

    var responseWrapper = new ResponseWrapper(HttpStatus.OK, result);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<ResponseWrapper> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
    var result = authenticationService.refreshToken(refreshTokenRequestDto);

    var responseWrapper = new ResponseWrapper(HttpStatus.OK, result);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }

  @GetMapping("/verify-email")
  public ResponseEntity<ResponseWrapper> verifyEmail(@RequestParam("signupKey") String signupKey) {
    boolean verified = authenticationService.verifyEmail(signupKey);
    HttpStatus httpStatus;
    String message;

    if (verified) {
      httpStatus = HttpStatus.OK;
      message = "Email verification successful";
    } else {
      httpStatus = HttpStatus.NOT_FOUND;
      message = "Invalid signup key!";
    }

    var responseWrapper = new ResponseWrapper(httpStatus, message);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }
}
