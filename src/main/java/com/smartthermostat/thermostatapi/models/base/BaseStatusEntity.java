package com.smartthermostat.thermostatapi.models.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartthermostat.thermostatapi.models.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseStatusEntity extends BaseEntity {

  @NotNull
  @Enumerated(EnumType.STRING)
  public Status status;

  @JsonIgnore
  public boolean isDeleted() {
    return Status.DELETED == status;
  }

  @JsonIgnore
  public boolean isActive() {
    return Status.ACTIVE == status;
  }
  public void setStatus(Status status) {
    this.status = status;
  }
}