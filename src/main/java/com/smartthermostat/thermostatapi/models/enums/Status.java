package com.smartthermostat.thermostatapi.models.enums;

public enum Status {
  ACTIVE(1, "ACTIVE"),
  INACTIVE(2, "INACTIVE"),
  DELETED(3, "DELETED");

  private final int id;
  private final String name;

  Status(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}