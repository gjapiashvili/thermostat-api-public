package com.smartthermostat.thermostatapi.config.security.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  public CustomAuthenticationEntryPoint() {
  }

  @Override
  public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    HttpStatus status = HttpStatus.UNAUTHORIZED;

    final Map<String, Object> body = new HashMap<>();

    if (authenticationException instanceof InsufficientAuthenticationException) {
      body.put("message", "You need to login first in order to perform this action");
    } else if (authenticationException instanceof InvalidBearerTokenException) {
      body.put("message", "Expired or Invalid token");
    }

    body.put("statusCode", status.value());
    body.put("status", status.name());
    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }
}
