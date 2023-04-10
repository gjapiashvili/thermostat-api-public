package com.smartthermostat.thermostatapi.models;

import com.smartthermostat.thermostatapi.models.base.BaseStatusAuditEntity;
import com.smartthermostat.thermostatapi.models.enums.Authority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseStatusAuditEntity implements UserDetails {
  private String firstname;
  private String lastname;
  @NonNull
  private String email;
  @NonNull
  private String password;
  private String signupKey;
  private boolean verified;
  private boolean receiveAlerts;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  private Set<Authority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities.stream()
        .map(authority -> new SimpleGrantedAuthority(authority.name()))
        .collect(Collectors.toSet());
  }
  public void addAuthority(Authority authority) {
    this.authorities.add(authority);
  }

  public void removeAuthority(Authority authority) {
    this.authorities.remove(authority);
  }

  @Override
  public @NonNull String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    User user = (User) o;
    return getId() != null && Objects.equals(getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public boolean getVerified() {
    return verified;
  }

  public String getIdStr() {
    return id.toString();
  }
}
