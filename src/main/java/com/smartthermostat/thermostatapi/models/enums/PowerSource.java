package com.smartthermostat.thermostatapi.models.enums;

import com.smartthermostat.thermostatapi.exceptions.CustomEnumValueNotFoundException;
import java.util.Arrays;

public enum PowerSource {
  HARDWIRED,
  BATTERY,
  HYBRID;

  public static PowerSource findByName(String name) {
    for (PowerSource powerSource: PowerSource.values()) {
      if (powerSource.name().equalsIgnoreCase(name)) {
        return powerSource;
      }
    }

    var valuesStr = Arrays.toString(PowerSource.values());
    throw new CustomEnumValueNotFoundException(PowerSource.class.getSimpleName(), name, valuesStr.substring(1, valuesStr.length() - 1));
  }
}
