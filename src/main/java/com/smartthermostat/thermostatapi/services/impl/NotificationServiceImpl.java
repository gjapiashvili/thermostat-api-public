package com.smartthermostat.thermostatapi.services.impl;

import com.smartthermostat.thermostatapi.models.Thermostat;
import com.smartthermostat.thermostatapi.models.User;
import com.smartthermostat.thermostatapi.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

  @Value("${server.port}")
  private String serverPort;

  @Value("${spring.mail.from}")
  private String sendEmailFrom;

  private final JavaMailSender mailSender;

  DecimalFormat df = new DecimalFormat("#.#");

  @Override
  public void sendMailVerificationEmail(User user) {
    var URL = "http://localhost:" + serverPort + "/api/v1/auth/verify-email?signupKey=" + user.getSignupKey();

    sendMail(
        user.getEmail(),
        "Verify your email",
        "To finalize your registration process please verify that this is your email address. " + URL
    );
  }

  @Override
  public void sendCriticalTemperatureAlert(Thermostat thermostat) {
    var brand = thermostat.getBrand().getName();
    double temperature = thermostat.getCurrentTemperature();

    String temperatureString;
    df.setRoundingMode(RoundingMode.HALF_UP);
    if (temperature % 1 == 0) {
      temperatureString = Integer.toString((int) temperature);
    } else {
      temperatureString = df.format(temperature);
    }

    var threshold = thermostat.getTemperatureThreshold();
    String thresholdStr;

    if (threshold % 1 == 0) {
      thresholdStr = Integer.toString((int) threshold);
    } else {
      thresholdStr = df.format(threshold);
    }

    var firstName = thermostat.getUser().getFirstname();

    if (firstName.isBlank()) {
      firstName = "valued customer";
    }

    String message = "Dear " + firstName + ",\n\nWe regret to inform you that the current temperature of your " + brand +
        " thermostat has exceeded the configured degree value of " + thresholdStr +
        " degrees Celsius. The current temperature is " + temperatureString +
        " degrees Celsius.\n\nThis may indicate an issue with your thermostat, " +
        "and we kindly ask you to take necessary steps to resolve the issue as soon as possible." +
        "\n\nIf you require any assistance, please do not hesitate to contact our customer support team." +
        "\n\nBest regards,\n\nThe SmartThermostats team";
    sendMail(thermostat.getUser().getEmail(), "Urgent: Action required for your " + brand + " thermostat", message);
  }

  private void sendMail(String sendTo, String subject, String text) {
    var simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(sendEmailFrom);
    simpleMailMessage.setTo(sendTo);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(text);
    mailSender.send(simpleMailMessage);
  }
}
