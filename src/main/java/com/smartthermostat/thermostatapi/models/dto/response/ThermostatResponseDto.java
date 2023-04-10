package com.smartthermostat.thermostatapi.models.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartthermostat.thermostatapi.models.Thermostat;
import com.smartthermostat.thermostatapi.models.enums.Brand;
import com.smartthermostat.thermostatapi.models.enums.PowerSource;
import lombok.Getter;
import lombok.Setter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ThermostatResponseDto {
  @JsonIgnore
  private final String simpleDateFormat = "yyyy-MM-dd HH:mm.ss";

  private Long ownerUserId;
  private Long thermostatId;
  private Brand brand;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double temperatureThreshold;
  private PowerSource powerSource;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double currentTemperature;
  private boolean isCritical;
  private String createdAt;
  private String updatedAt;

  public ThermostatResponseDto(Thermostat thermostat) {
    this.ownerUserId = thermostat.getUser().getId();
    this.thermostatId = thermostat.getId();
    this.brand = thermostat.getBrand();
    this.temperatureThreshold = thermostat.getTemperatureThreshold();
    this.powerSource = thermostat.getPowerSource();
    this.currentTemperature = thermostat.getCurrentTemperature();
    this.isCritical = thermostat.getCurrentTemperature() > thermostat.getTemperatureThreshold();
    this.createdAt = new SimpleDateFormat(simpleDateFormat).format(thermostat.getCreatedAt());
    this.updatedAt = new SimpleDateFormat(simpleDateFormat).format(thermostat.getUpdatedAt());
  }
  public static List<ThermostatResponseDto> toList(List<Thermostat> thermostats) {
    List<ThermostatResponseDto> retList = new ArrayList<>();

    for (Thermostat thermostat: thermostats) {
      retList.add(new ThermostatResponseDto(thermostat));
    }

    return retList;
  }
}