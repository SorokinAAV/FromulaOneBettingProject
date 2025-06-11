package com.artemsorokin.util;

import com.artemsorokin.model.Driver;
import com.artemsorokin.model.F1DriverResponse;
import com.artemsorokin.model.OpenF1SessionResponse;
import com.artemsorokin.model.SessionF1;

import java.util.List;
import java.util.Random;

public class F1Utils {
  public static SessionF1 mapSession(OpenF1SessionResponse sessionResponse) {
    return SessionF1.builder()
        .sessionName(sessionResponse.getSession_name())
        .sessionType(sessionResponse.getSession_type())
        .year(String.valueOf(sessionResponse.getYear()))
        .country(sessionResponse.getCountry_name())
        .date_start(sessionResponse.getDate_start())
        .date_end(sessionResponse.getDate_end())
        .location(sessionResponse.getLocation())
        .sessionKey(sessionResponse.getSession_key())
        .build();
  }

  public static List<Driver> mapDrivers(List<F1DriverResponse> driverResponses) {
    return driverResponses.stream()
        .map(driverResponse -> Driver.builder()
            .fullName(driverResponse.getFull_name())
            .driverId(String.valueOf(driverResponse.getDriver_number()))
            .odds(2 + new Random().nextInt(3)) // Random odds between 2 and 4
            .build())
        .toList();
  }
}
