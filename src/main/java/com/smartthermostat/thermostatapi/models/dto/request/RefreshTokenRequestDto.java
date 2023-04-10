package com.smartthermostat.thermostatapi.models.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDto {
  private String refreshToken;
}
