package com.smartthermostat.thermostatapi.jobs;

import com.smartthermostat.thermostatapi.models.Thermostat;
import com.smartthermostat.thermostatapi.models.enums.Status;
import com.smartthermostat.thermostatapi.repositories.ThermostatRepository;
import com.smartthermostat.thermostatapi.services.NotificationService;
import com.smartthermostat.thermostatapi.services.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ThermostatScheduler {

  private final ThermostatRepository thermostatRepository;
  private final NotificationServiceImpl notificationService;

  @Scheduled(fixedDelay = 5000) // run every 5 seconds
  public void checkTemperature() {
    List<Thermostat> thermostats = thermostatRepository.findAllActive();

    for (Thermostat thermostat : thermostats) {
      if (!thermostat.getUser().isReceiveAlerts()) {
        continue;
      }

      double currentTemperature = thermostat.getCurrentTemperature();
      double temperatureThreshold = thermostat.getTemperatureThreshold();

      if (currentTemperature > temperatureThreshold) {
        notificationService.sendCriticalTemperatureAlert(thermostat);
      }
    }
  }
}