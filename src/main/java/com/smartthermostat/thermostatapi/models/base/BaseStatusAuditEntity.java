package com.smartthermostat.thermostatapi.models.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartthermostat.thermostatapi.models.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseStatusAuditEntity extends BaseStatusEntity {

  @JsonIgnore
  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  protected Date createdAt;

  @JsonIgnore
  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  protected Date updatedAt;

  @PrePersist
  public void prePersist() {
    createdAt = new Date();
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}