package com.smartthermostat.thermostatapi.models.enums;

import com.smartthermostat.thermostatapi.exceptions.CustomEnumValueNotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Brand {
  AMAZON("Amazon"),
  ECOBEE("Ecobee"),
  HONEYWELL("Honeywell"),
  WYZE("Wyze"),
  EMERSON("Emerson");

  private final String name;

  Brand(String name) {
    this.name = name;
  }
  public static Brand findByName(String name) {
    for (Brand brand: Brand.values()) {
      if (brand.name().equalsIgnoreCase(name)) {
        return brand;
      }
    }

    var valuesStr = Arrays.toString(Brand.values());
    throw new CustomEnumValueNotFoundException(Brand.class.getSimpleName(), name, valuesStr.substring(1, valuesStr.length() - 1));
  }
}
