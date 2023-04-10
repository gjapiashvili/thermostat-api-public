package com.smartthermostat.thermostatapi.config.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.smartthermostat.thermostatapi.config.security.exceptions.CustomAccessDeniedHandler;
import com.smartthermostat.thermostatapi.config.security.exceptions.CustomAuthenticationEntryPoint;
import com.smartthermostat.thermostatapi.models.enums.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
@Slf4j
public class WebSecurity {
  private final JwtToUserConverter jwtToUserConverter;
  private final KeyUtils keyUtils;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/api/v1/auth/**").permitAll() //Whitelisting this, all others have to be authenticated, see anyRequest().authenticated() below
            .requestMatchers("/api/v1/admin/**").hasAuthority(Authority.ADMIN.name()) //Only admins can access this
            .anyRequest().authenticated()
        )
        .csrf().disable()
        .cors().disable()
        .httpBasic().disable()
        .oauth2ResourceServer((oauth2) ->
            oauth2.jwt((jwt) -> jwt.jwtAuthenticationConverter(jwtToUserConverter))
                //The line below makes sure that we're using CustomAuthenticationEntryPoint.commence() for
                //expired or invalid token instead of default BearerTokenAuthenticationEntryPoint.commence()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        )
        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling((exceptions) -> exceptions
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) //For empty invalid token
            .accessDeniedHandler(new CustomAccessDeniedHandler()) //For the case when user doesn't have required role
        );
    return http.build();
  }

  @Bean
  @Primary
  JwtDecoder jwtAccessTokenDecoder() {
    return NimbusJwtDecoder.withPublicKey(keyUtils.getAccessTokenPublicKey()).build();
  }

  @Bean
  @Primary
  JwtEncoder jwtAccessTokenEncoder() {
    JWK jwk = new RSAKey
        .Builder(keyUtils.getAccessTokenPublicKey())
        .privateKey(keyUtils.getAccessTokenPrivateKey())
        .build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  @Qualifier("jwtRefreshTokenDecoder")
  JwtDecoder jwtRefreshTokenDecoder() {
    return NimbusJwtDecoder.withPublicKey(keyUtils.getRefreshTokenPublicKey()).build();
  }

  @Bean
  @Qualifier("jwtRefreshTokenEncoder")
  JwtEncoder jwtRefreshTokenEncoder() {
    JWK jwk = new RSAKey
        .Builder(keyUtils.getRefreshTokenPublicKey())
        .privateKey(keyUtils.getRefreshTokenPrivateKey())
        .build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  @Qualifier("jwtRefreshTokenAuthProvider")
  JwtAuthenticationProvider jwtRefreshTokenAuthProvider() {
    JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtRefreshTokenDecoder());
    provider.setJwtAuthenticationConverter(jwtToUserConverter);
    return provider;
  }
}
