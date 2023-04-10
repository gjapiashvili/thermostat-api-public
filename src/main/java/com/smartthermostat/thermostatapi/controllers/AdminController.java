package com.smartthermostat.thermostatapi.controllers;

import com.smartthermostat.thermostatapi.controllers.config.ResponseWrapper;
import com.smartthermostat.thermostatapi.models.Thermostat;
import com.smartthermostat.thermostatapi.models.dto.admin.UserResponseAdminDto;
import com.smartthermostat.thermostatapi.models.dto.response.ThermostatResponseDto;
import com.smartthermostat.thermostatapi.services.UserService;
import com.smartthermostat.thermostatapi.services.impl.ThermostatServiceImpl;
import com.smartthermostat.thermostatapi.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.smartthermostat.thermostatapi.controllers.config.ControllerBase.ADMIN_ENTRY_POINT;
import static com.smartthermostat.thermostatapi.controllers.config.ControllerBase.ENTRY_POINT_V1;

@RestController
@RequestMapping(path = ENTRY_POINT_V1 + ADMIN_ENTRY_POINT)
@RequiredArgsConstructor
public class AdminController {

  private final UserServiceImpl userService;
  private final ThermostatServiceImpl thermostatService;

  @GetMapping("/users")
  //No need for method level security because we're securing the whole "/api/v1/admin/**" in SecurityConfiguration > securityFilterChain()
  //@PreAuthorize("hasAuthority('ADMIN')") //We need to have @EnableMethodSecurity in SecurityConfiguration for this to work
  public ResponseEntity<ResponseWrapper> findAllUsers() {
    var result = userService.findAllUsers();
    var responseWrapper = new ResponseWrapper(HttpStatus.OK, UserResponseAdminDto.toList(result));
    return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<ResponseWrapper> user(@PathVariable Long id) {
    var resultOptional = userService.findUserById(id);
    UserResponseAdminDto result = null;
    HttpStatus httpStatus;

    if (resultOptional.isPresent()) {
      result = new UserResponseAdminDto(resultOptional.get());
      httpStatus = HttpStatus.OK;
    } else {
      httpStatus = HttpStatus.NOT_FOUND;
    }

    var responseWrapper = new ResponseWrapper(httpStatus, result);
    return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
  }

  @PutMapping("/users/{id}/assign-admin-rights")
  public ResponseEntity<ResponseWrapper> assignAdminRights(@PathVariable Long id) {
    var user = userService.assignAdminRights(id);
    HttpStatus httpStatus;

    if (user != null) {
      httpStatus = HttpStatus.OK;
    } else {
      httpStatus = HttpStatus.NOT_FOUND;
    }

    var responseWrapper = new ResponseWrapper(httpStatus, new UserResponseAdminDto(user));
    return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
  }

  @PutMapping("/users/{id}/revoke-admin-rights")
  public ResponseEntity<ResponseWrapper> revokeAdminRights(@PathVariable Long id) {
    var user = userService.revokeAdminRights(id);
    HttpStatus httpStatus;

    if (user != null) {
      httpStatus = HttpStatus.OK;
    } else {
      httpStatus = HttpStatus.NOT_FOUND;
    }

    var responseWrapper = new ResponseWrapper(httpStatus, new UserResponseAdminDto(user));
    return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
  }

  @GetMapping("/thermostats")
  public ResponseEntity<ResponseWrapper> getAllThermostatsInfo() {
    var thermostats = thermostatService.getAllThermostats();
    var result = ThermostatResponseDto.toList(thermostats);

    var responseWrapper = new ResponseWrapper(HttpStatus.OK, result);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }

}
