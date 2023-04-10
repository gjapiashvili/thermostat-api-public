package com.smartthermostat.thermostatapi.config.security;

import com.smartthermostat.thermostatapi.exceptions.CustomException;
import com.smartthermostat.thermostatapi.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
  private final UserServiceImpl userService;

  @Override
  public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
    String tokenType = jwt.getClaim("type");

    if (tokenType != null && tokenType.equals("access")) {
      List<Map<String, Object>> authoritiesList = jwt.getClaim("authorities");

      // create a collection of SimpleGrantedAuthority objects
      Collection<SimpleGrantedAuthority> grantedAuthorities = authoritiesList.stream()
          .map(authority -> new SimpleGrantedAuthority((String) authority.get("role")))
          .collect(Collectors.toList());

      // extract user information from the JWT
      String userId = jwt.getSubject();

      var user = userService.findUserById(userId).orElseThrow(() -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while trying to find a user"));

      return new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);
    } else if (tokenType != null && tokenType.equals("refresh")) {
      String userId = jwt.getSubject();
      var user = userService.findUserById(userId).orElseThrow(() -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while trying to find a user"));
      return new UsernamePasswordAuthenticationToken(user, jwt, null);
    } else {
      throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid token type");
    }
  }

}
