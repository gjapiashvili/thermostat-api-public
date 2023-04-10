package com.smartthermostat.thermostatapi.services;

import com.smartthermostat.thermostatapi.models.Thermostat;
import com.smartthermostat.thermostatapi.models.dto.request.ThermostatCreateRequestDto;

import java.util.List;

public interface ThermostatService {
  Thermostat createThermostat(ThermostatCreateRequestDto thermostatCreateRequest);

  List<Thermostat> getMyThermostats();

  List<Thermostat> getAllThermostats();

  boolean deleteThermostatById(Long thermostatId);
}
