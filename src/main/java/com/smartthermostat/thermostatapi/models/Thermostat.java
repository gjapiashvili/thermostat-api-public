package com.smartthermostat.thermostatapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartthermostat.thermostatapi.models.base.BaseStatusAuditEntity;
import com.smartthermostat.thermostatapi.models.enums.Brand;
import com.smartthermostat.thermostatapi.models.enums.PowerSource;
import com.smartthermostat.thermostatapi.models.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "thermostats")
public class Thermostat extends BaseStatusAuditEntity {
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(EnumType.STRING)
  private Brand brand;

  private double currentTemperature;

  private double temperatureThreshold;

  @Enumerated(EnumType.STRING)
  private PowerSource powerSource;
}
