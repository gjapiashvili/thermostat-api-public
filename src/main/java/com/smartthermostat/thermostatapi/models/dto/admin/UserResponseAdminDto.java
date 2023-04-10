package com.smartthermostat.thermostatapi.models.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartthermostat.thermostatapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseAdminDto {
  private Long Id;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String firstname;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String lastname;
  private String email;
  private Collection<? extends GrantedAuthority> authorities;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String signupKey;
  private boolean verified;

  public UserResponseAdminDto(User user) {
    if (user != null) {
      this.Id = user.getId();
      this.firstname = user.getFirstname();
      this.lastname = user.getLastname();
      this.email = user.getEmail();
      this.authorities = user.getAuthorities();
      this.signupKey = user.getSignupKey();
      this.verified = user.getVerified();
    }
  }
  public static List<UserResponseAdminDto> toList(List<User> users) {
    List<UserResponseAdminDto> retList = new ArrayList<>();

    for (User user: users) {
      retList.add(new UserResponseAdminDto(user));
    }

    return retList;
  }
}
