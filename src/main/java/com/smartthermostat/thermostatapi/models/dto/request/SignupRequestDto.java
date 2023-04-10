package com.smartthermostat.thermostatapi.models.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SignupRequestDto implements Serializable {
  private String firstname;
  private String lastname;
  private String email;
  private String password;
}
