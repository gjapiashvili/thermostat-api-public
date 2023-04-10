package com.smartthermostat.thermostatapi.controllers;

import com.smartthermostat.thermostatapi.controllers.config.ResponseWrapper;
import com.smartthermostat.thermostatapi.models.dto.request.UpdateProfileRequestDto;
import com.smartthermostat.thermostatapi.models.dto.response.UserResponseDto;
import com.smartthermostat.thermostatapi.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.smartthermostat.thermostatapi.controllers.config.ControllerBase.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ENTRY_POINT_V1 + USER_ENTRY_POINT)
public class UserController {

  private final UserServiceImpl userService;

  @GetMapping("/me")
  public ResponseEntity<ResponseWrapper> getMe() {
    var user = userService.getLoggedInUser();
    var result = new UserResponseDto(user);

    var responseWrapper = new ResponseWrapper(HttpStatus.OK, result);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }

  @DeleteMapping("/me/deactivate-account")
  public ResponseEntity<ResponseWrapper> deleteProfile() {
    userService.deactivateLoggedInUserProfile();
    var responseWrapper = new ResponseWrapper(HttpStatus.NO_CONTENT);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }

  @PutMapping("/me/update-profile")
  public ResponseEntity<ResponseWrapper> editProfile(@RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
    var updatedUser = userService.updateLoggedInUserProfile(updateProfileRequestDto);

    var result = new UserResponseDto(updatedUser);
    var responseWrapper = new ResponseWrapper(HttpStatus.OK, result);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }
}
