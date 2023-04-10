package com.smartthermostat.thermostatapi.repositories;

import com.smartthermostat.thermostatapi.models.Thermostat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThermostatRepository extends JpaRepository<Thermostat, Long> {

  @Query("select t from Thermostat t where t.user.id = ?1 and t.status = com.smartthermostat.thermostatapi.models.enums.Status.ACTIVE")
  List<Thermostat> findActiveByUserId(Long userId);

  @Query("select t from Thermostat t where t.status = com.smartthermostat.thermostatapi.models.enums.Status.ACTIVE")
  List<Thermostat> findAllActive();
}
