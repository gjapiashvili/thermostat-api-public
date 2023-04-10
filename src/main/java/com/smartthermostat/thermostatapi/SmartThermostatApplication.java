package com.smartthermostat.thermostatapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartThermostatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartThermostatApplication.class, args);
	}

}
