package com.smartthermostat.thermostatapi.config.security;

import com.smartthermostat.thermostatapi.models.User;
import com.smartthermostat.thermostatapi.models.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class TokenGenerator {

  @Autowired
  private JwtEncoder accessTokenEncoder;

  @Autowired
  @Qualifier("jwtRefreshTokenEncoder")
  private JwtEncoder refreshTokenEncoder;

  @Value("${spring.application-name}")
  private String applicationName;

  @Value("${access-token.expires-minutes}")
  private Long accessTokenExpiresMinutes;

  @Value("${refresh-token.expires-days}")
  private Long refreshTokenExpiresDays;

  private String createAccessToken(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Instant now = Instant.now();

    JwtClaimsSet claimsSet = JwtClaimsSet.builder()
        .issuer("SmartThermostats")
        .issuedAt(now)
        .claim("type", "access")
        .expiresAt(now.plus(accessTokenExpiresMinutes, ChronoUnit.MINUTES))
        .subject(user.getIdStr()) //We're using user ID as subject, better than using email because it may change in the future
        .claim("authorities", user.getAuthorities())
        .build();

    return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
  }

  private String createRefreshToken(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Instant now = Instant.now();

    JwtClaimsSet claimsSet = JwtClaimsSet.builder()
        .issuer(applicationName)
        .issuedAt(now)
        .claim("type", "refresh")
        .expiresAt(now.plus(refreshTokenExpiresDays, ChronoUnit.DAYS))
        .subject(user.getIdStr())
        .build();

    return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
  }

  public TokenDTO createToken(Authentication authentication) {
    if (!(authentication.getPrincipal() instanceof User user)) {
      throw new BadCredentialsException(
          MessageFormat.format("principal {0} is not of User type", authentication.getPrincipal().getClass())
      );
    }

    TokenDTO tokenDto = new TokenDTO();
    tokenDto.setUserId(user.getIdStr());
    tokenDto.setAccessToken(createAccessToken(authentication));

    String refreshToken;
    if (authentication.getCredentials() instanceof Jwt jwt) {
      Instant now = Instant.now();
      Instant expiresAt = jwt.getExpiresAt();
      Duration duration = Duration.between(now, expiresAt);
      long daysUntilExpired = duration.toDays();
      if (daysUntilExpired < 7) {
        refreshToken = createRefreshToken(authentication);
      } else {
        refreshToken = jwt.getTokenValue();
      }
    } else {
      refreshToken = createRefreshToken(authentication);
    }
    tokenDto.setRefreshToken(refreshToken);

    return tokenDto;
  }
}
