package com.smartthermostat.thermostatapi.controllers;

import com.smartthermostat.thermostatapi.controllers.config.ResponseWrapper;
import com.smartthermostat.thermostatapi.models.Thermostat;
import com.smartthermostat.thermostatapi.models.dto.request.ThermostatCreateRequestDto;
import com.smartthermostat.thermostatapi.models.dto.response.ThermostatResponseDto;
import com.smartthermostat.thermostatapi.services.impl.ThermostatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.smartthermostat.thermostatapi.controllers.config.ControllerBase.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ENTRY_POINT_V1 + THERMOSTAT_ENTRY_POINT)
public class ThermostatController {
  private final ThermostatServiceImpl thermostatService;

  @PostMapping
  public ResponseEntity<ResponseWrapper> createThermostat(@RequestBody ThermostatCreateRequestDto thermostatCreateRequest) {
    var thermostat = thermostatService.createThermostat(thermostatCreateRequest);
    var thermostatResponseDto = new ThermostatResponseDto(thermostat);

    var responseWrapper = new ResponseWrapper(HttpStatus.CREATED, thermostatResponseDto);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }

  @GetMapping
  public ResponseEntity<ResponseWrapper> getMyThermostats() {
    List<Thermostat> thermostats = thermostatService.getMyThermostats();
    var result = ThermostatResponseDto.toList(thermostats);

    var responseWrapper = new ResponseWrapper(HttpStatus.OK, result);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }

  @DeleteMapping("/{thermostatId}")
  public ResponseEntity<ResponseWrapper> deleteThermostat(@PathVariable Long thermostatId) {
    String message;
    HttpStatus httpStatus;
    var deleted = thermostatService.deleteThermostatById(thermostatId);

    if (deleted) {
      message = "The thermostat with the specified ID has been successfully deleted from the system.";
      httpStatus = HttpStatus.OK;
    } else {
      message = "The thermostat with ID " + thermostatId + " could not be found, or it does not belong to the current user.";
      httpStatus = HttpStatus.NOT_FOUND;
    }

    var responseWrapper = new ResponseWrapper(httpStatus, message);
    return ResponseEntity.status(responseWrapper.getHttpStatus()).body(responseWrapper);
  }
}
