package com.smartthermostat.thermostatapi.services;


import com.smartthermostat.thermostatapi.models.Thermostat;
import com.smartthermostat.thermostatapi.models.User;

public interface NotificationService {
  void sendMailVerificationEmail(User user);
  void sendCriticalTemperatureAlert(Thermostat thermostat);
}
