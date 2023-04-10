package com.smartthermostat.thermostatapi.models.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThermostatCreateRequestDto {
  private String brand;
  private Double temperatureThreshold;
  public String powerSource;
}
