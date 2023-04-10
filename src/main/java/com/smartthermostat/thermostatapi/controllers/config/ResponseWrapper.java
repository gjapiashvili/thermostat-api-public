package com.smartthermostat.thermostatapi.controllers.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ResponseWrapper {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object data = null;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer statusCode = null;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String status = null;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String message = null;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String jwt = null;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private final LocalDateTime timestamp;

  public ResponseWrapper() {
    this.timestamp = LocalDateTime.now();
  }

  public ResponseWrapper(HttpStatus httpStatus) {
    this();
    this.statusCode = httpStatus.value();
    this.status = httpStatus.name();
  }

  public ResponseWrapper(HttpStatus httpStatus, String message) {
    this(httpStatus);
    this.message = message;
  }

  public ResponseWrapper(HttpStatus httpStatus, Object data) {
    this(httpStatus);
    this.data = data;
  }

  public ResponseWrapper(HttpStatus httpStatus, Object data, String message) {
    this(httpStatus, data);
    this.message = message;
  }

  public ResponseWrapper(HttpStatus httpStatus, Object data, String message, String jwt) {
    this(httpStatus, data, message);
    this.jwt = jwt;
  }

  @JsonIgnore
  public HttpStatus getHttpStatus() {
    return HttpStatus.valueOf(this.statusCode);
  }
}
