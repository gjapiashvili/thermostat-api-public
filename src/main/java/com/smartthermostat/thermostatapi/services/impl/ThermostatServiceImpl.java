package com.smartthermostat.thermostatapi.services.impl;

import com.smartthermostat.thermostatapi.models.Thermostat;
import com.smartthermostat.thermostatapi.models.dto.request.ThermostatCreateRequestDto;
import com.smartthermostat.thermostatapi.models.enums.Brand;
import com.smartthermostat.thermostatapi.models.enums.PowerSource;
import com.smartthermostat.thermostatapi.models.enums.Status;
import com.smartthermostat.thermostatapi.repositories.ThermostatRepository;
import com.smartthermostat.thermostatapi.services.ThermostatService;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ThermostatServiceImpl implements ThermostatService {
  private final UserServiceImpl userService;
  private final ThermostatRepository thermostatRepository;
  @PostUpdate
  public void onTemperatureChange(Thermostat thermostat) {
    // do something with the updated thermostat object
    // for example, call a method on a service or repository
  }

  @Override
  public Thermostat createThermostat(ThermostatCreateRequestDto request) {
    var currentUser = userService.getLoggedInUser();

    Thermostat thermostat = new Thermostat();
    thermostat.setUser(currentUser);
    thermostat.setBrand(Brand.findByName(request.getBrand()));
    thermostat.setTemperatureThreshold(request.getTemperatureThreshold());
    thermostat.setPowerSource(PowerSource.findByName(request.getPowerSource()));
    thermostat.setCreatedAt(new Date());
    thermostat.setUpdatedAt(new Date());
    thermostat.setStatus(Status.ACTIVE);
    saveThermostat(thermostat);

    return thermostat;
  }

  @Override
  public List<Thermostat> getMyThermostats() {
    var currentUser = userService.getLoggedInUser();
    return thermostatRepository.findActiveByUserId(currentUser.getId());
  }

  @Override
  public List<Thermostat> getAllThermostats() {
    return thermostatRepository.findAllActive();
  }

  @Override
  public boolean deleteThermostatById(Long thermostatId) {
    var currentUser = userService.getLoggedInUser();
    var thermostatOptional = thermostatRepository.findById(thermostatId);
    if (thermostatOptional.isEmpty() ||
        !thermostatOptional.get().getUser().getId().equals(currentUser.getId())) {
      return false;
    }

    thermostatOptional.get().setStatus(Status.DELETED);
    saveThermostat(thermostatOptional.get());
    return true;
  }

  public Thermostat saveThermostat(Thermostat thermostat) {
    return thermostatRepository.save(thermostat);
  }
}
