package com.smartthermostat.thermostatapi.exceptions;

import com.smartthermostat.thermostatapi.controllers.config.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = {CustomException.class})
  public ResponseEntity<ResponseWrapper> handleCustomException(CustomException ex) {
    var response = new ResponseWrapper(ex.getHttpStatus(), ex.getMessage());
   return ResponseEntity.status(ex.getHttpStatus()).body(response);
  }

  @ExceptionHandler(value = {CustomLoginNeededException.class})
  public ResponseEntity<ResponseWrapper> handleCustomLoginNeededException(CustomLoginNeededException ex) {
    var response = new ResponseWrapper(ex.getHttpStatus(), ex.getMessage());
    return ResponseEntity.status(ex.getHttpStatus()).body(response);
  }

  @ExceptionHandler(value = {CustomEnumValueNotFoundException.class})
  public ResponseEntity<ResponseWrapper> handleCustomEnumValueNotFoundException(CustomEnumValueNotFoundException ex) {
    var response = new ResponseWrapper(ex.getHttpStatus(), ex.getMessage());
    return ResponseEntity.status(ex.getHttpStatus()).body(response);
  }
}