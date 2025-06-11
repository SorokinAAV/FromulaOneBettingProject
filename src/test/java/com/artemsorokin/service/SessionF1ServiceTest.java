package com.artemsorokin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.artemsorokin.Util.F1Utils;
import com.artemsorokin.client.OpenF1ApiClient;
import com.artemsorokin.model.Driver;
import com.artemsorokin.model.F1DriverResponse;
import com.artemsorokin.model.OpenF1SessionResponse;
import com.artemsorokin.model.SessionF1;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SessionF1ServiceTest {

  @Mock private OpenF1ApiClient openF1ApiClient;

  @InjectMocks private SessionF1Service sessionF1Service;

  @BeforeEach
  void setUp() {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFetchSessions() {
    OpenF1SessionResponse sessionResponse = new OpenF1SessionResponse();
    sessionResponse.setSession_key(123);

    F1DriverResponse driverResponse = new F1DriverResponse();
    Driver driver = Driver.builder().build();

    SessionF1 mappedSession = SessionF1.builder().build();

    // Mock static calls
    try (var mockedUtils = mockStatic(F1Utils.class)) {
      mockedUtils.when(() -> F1Utils.mapSession(sessionResponse)).thenReturn(mappedSession);
      mockedUtils
          .when(() -> F1Utils.mapDrivers(List.of(driverResponse)))
          .thenReturn(List.of(driver));

      // Mock API client calls
      when(openF1ApiClient.getSessions(anyString(), anyString(), anyString()))
          .thenReturn(List.of(sessionResponse));
      when(openF1ApiClient.getDrivers(123)).thenReturn(List.of(driverResponse));

      // Act
      var sessions = sessionF1Service.fetchSessions("USA", "RACE", "2023");

      // Assert
      assertEquals(1, sessions.size());
      assertEquals(mappedSession, sessions.get(0));
      assertEquals(List.of(driver), sessions.get(0).getDrivers());

      verify(openF1ApiClient).getSessions("USA", "RACE", "2023");
      verify(openF1ApiClient).getDrivers(123);
    }
  }

  @Test
  void testGetSessionByKey() {
    // Arrange
    int sessionKey = 123;

    OpenF1SessionResponse sessionResponse = new OpenF1SessionResponse();
    sessionResponse.setSession_key(sessionKey);

    F1DriverResponse driverResponse = new F1DriverResponse();
    Driver driver = Driver.builder().build();

    SessionF1 mappedSession = SessionF1.builder().build();

    try (var mockedUtils = mockStatic(F1Utils.class)) {
      mockedUtils.when(() -> F1Utils.mapSession(sessionResponse)).thenReturn(mappedSession);
      mockedUtils
          .when(() -> F1Utils.mapDrivers(List.of(driverResponse)))
          .thenReturn(List.of(driver));

      // Mock behavior for OpenF1ApiClient
      when(openF1ApiClient.getSessionByKey(sessionKey)).thenReturn(List.of(sessionResponse));
      when(openF1ApiClient.getDrivers(sessionKey)).thenReturn(List.of(driverResponse));

      // Act
      List<SessionF1> sessions = sessionF1Service.getSessionByKey(sessionKey);

      // Assert
      assertEquals(1, sessions.size());
      assertEquals(mappedSession, sessions.get(0));
      assertEquals(List.of(driver), sessions.get(0).getDrivers());

      // Verify mock interactions
      verify(openF1ApiClient).getSessionByKey(sessionKey);
      verify(openF1ApiClient).getDrivers(sessionKey);
    }
  }

  @Test
  void testFetchSessions_NoSessions() {
    // Arrange
    String countryName = "USA";
    String sessionType = "QUALIFYING";
    String year = "2023";

    // Mock behavior
    when(openF1ApiClient.getSessions(countryName, sessionType, year))
        .thenReturn(Collections.emptyList());

    // Act
    List<SessionF1> sessions = sessionF1Service.fetchSessions(countryName, sessionType, year);

    // Assert
    assertEquals(0, sessions.size());

    verify(openF1ApiClient).getSessions(countryName, sessionType, year);
    verifyNoMoreInteractions(openF1ApiClient);
  }

  @Test
  void testFetchSessions_NullSessionResponses() {
    // Arrange
    String countryName = "France";
    String sessionType = "RACE";
    String year = "2022";

    // Mock behavior
    when(openF1ApiClient.getSessions(countryName, sessionType, year)).thenReturn(null);

    // Act
    List<SessionF1> sessions = sessionF1Service.fetchSessions(countryName, sessionType, year);

    // Assert
    assertEquals(0, sessions.size());

    verify(openF1ApiClient).getSessions(countryName, sessionType, year);
    verifyNoMoreInteractions(openF1ApiClient);
  }
}
