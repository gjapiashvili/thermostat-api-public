package com.smartthermostat.thermostatapi.models.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequestDto {
  private String firstName;
  private String lastName;
  private boolean receiveAlerts;
}
