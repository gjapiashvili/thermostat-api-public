package com.smartthermostat.thermostatapi.models.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartthermostat.thermostatapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
  private Long Id;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String firstname;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String lastname;
  private String email;

  public UserResponseDto(User user) {
    this.Id = user.getId();
    this.firstname = user.getFirstname();
    this.lastname = user.getLastname();
    this.email = user.getEmail();
  }
  public static List<UserResponseDto> toList(List<User> users) {
    List<UserResponseDto> retList = new ArrayList<>();

    for (User user: users) {
      retList.add(new UserResponseDto(user));
    }

    return retList;
  }
}
