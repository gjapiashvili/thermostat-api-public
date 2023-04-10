package com.smartthermostat.thermostatapi.exceptions;

import org.springframework.http.HttpStatus;

public class CustomEnumValueNotFoundException extends RuntimeException {
  private final String message;
  private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

  public CustomEnumValueNotFoundException(String enumName, String providedValue, String validValues) {
    this.message = enumName + " with provided value (" + providedValue +
        ") was not found! Valid values are: " + validValues
        + ". The values are not case sensitive.";
  }

  @Override
  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}
