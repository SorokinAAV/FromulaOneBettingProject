package com.artemsorokin.service;

import static com.artemsorokin.Util.F1Utils.mapDrivers;
import static com.artemsorokin.Util.F1Utils.mapSession;

import com.artemsorokin.client.OpenF1ApiClient;
import com.artemsorokin.model.Driver;
import com.artemsorokin.model.F1DriverResponse;
import com.artemsorokin.model.OpenF1SessionResponse;
import com.artemsorokin.model.SessionF1;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SessionF1Service {

  private final OpenF1ApiClient openF1ApiClient;

  public List<SessionF1> fetchSessions(String countryName, String sessionType, String year) {
    List<OpenF1SessionResponse> rawSessions =
        openF1ApiClient.getSessions(countryName, sessionType, year);

    return mapToSessionsWithDrivers(rawSessions);
  }

  public List<SessionF1> getSessionByKey(int sessionKey) {
    var sessionResponses = openF1ApiClient.getSessionByKey(sessionKey);

    return mapToSessionsWithDrivers(sessionResponses);
  }

  private List<SessionF1> mapToSessionsWithDrivers(List<OpenF1SessionResponse> sessionResponses) {
    List<SessionF1> sessions = new ArrayList<>();
    if (sessionResponses != null) {
      for (OpenF1SessionResponse sessionResponse : sessionResponses) {
        var session = mapSession(sessionResponse);
        List<F1DriverResponse> driverResponses =
            openF1ApiClient.getDrivers(sessionResponse.getSession_key());
        List<Driver> drivers = null;
        if (driverResponses != null) {
          drivers = mapDrivers(driverResponses);
        }
        session.setDrivers(drivers);
        sessions.add(session);
      }
    }
    return sessions;
  }
}
