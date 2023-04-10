package com.smartthermostat.thermostatapi.exceptions;

import org.springframework.http.HttpStatus;

public class CustomLoginNeededException extends RuntimeException {
  private final String message;
  private final HttpStatus httpStatus;

  public CustomLoginNeededException() {
    this.message = "You need to login first in order to perform this action";
    this.httpStatus = HttpStatus.UNAUTHORIZED;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}