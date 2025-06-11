package com.artemsorokin.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.artemsorokin.exception.F1BetException;
import com.artemsorokin.model.*;
import com.artemsorokin.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EventOutcomeServiceImplTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private EventOutcomeServiceImpl eventOutcomeServiceImpl;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void processEventOutcome_ShouldProcessAllBets() {
    // Arrange
    EventOutcome eventOutcome = new EventOutcome(123, "driver1");

    Bet bet1 =
        new Bet(
            createSessionF1(123, List.of(createDriver("driver1", "DRIVER", 3))),
            "driver1",
            100,
            BetStatus.ACCEPTED);
    Bet bet2 =
        new Bet(
            createSessionF1(124, List.of(createDriver("driver2", "DRIVER", 2))),
            "driver2",
            50,
            BetStatus.ACCEPTED);

    Map<String, List<Bet>> allBets = Map.of("user1", List.of(bet1, bet2));

    User user = new User("user1", 500.0);

    when(userRepository.getAllBets()).thenReturn(allBets);
    when(userRepository.getUser("user1")).thenReturn(user);

    // Act
    String result = eventOutcomeServiceImpl.processEventOutcome(eventOutcome);

    // Assert
    assertEquals("Event outcome processed successfully!", result);
    assertEquals(BetStatus.WON, bet1.getBetStatus()); // Winning bet
    assertEquals(BetStatus.ACCEPTED, bet2.getBetStatus()); // Losing bet

    verify(userRepository)
        .updateUserBalance("user1", 800.0); // Updated balance = 500 (initial) + 300 (prize)
    verify(userRepository).getAllBets();
    verify(userRepository).getUser("user1");
  }

  @Test
  void processEventOutcome_NoBets() {
    // Arrange
    EventOutcome eventOutcome = new EventOutcome(123, "driver1");
    when(userRepository.getAllBets()).thenReturn(Collections.emptyMap());

    // Act
    String result = eventOutcomeServiceImpl.processEventOutcome(eventOutcome);

    // Assert
    assertEquals("Event outcome processed successfully!", result);
    verify(userRepository).getAllBets();
  }

  @Test
  void findWinnerAndPrize_ShouldHandleWinningBet() {
    // Arrange
    String userId = "user1";
    Bet bet =
        new Bet(
            createSessionF1(123, List.of(createDriver("driver1", "DRIVER", 3))),
            "driver1",
            100,
            BetStatus.ACCEPTED);
    User user = new User("user1", 500.0);

    when(userRepository.getUser(userId)).thenReturn(user);

    // Act
    eventOutcomeServiceImpl.findWinnerAndPrize(List.of(bet), 123, "driver1", userId);

    // Assert
    assertEquals(BetStatus.WON, bet.getBetStatus());
    verify(userRepository)
        .updateUserBalance("user1", 800.0); // Updated balance = 500 (initial) + 300 (prize)
    verify(userRepository).getUser("user1");
  }

  @Test
  void findWinnerAndPrize_ShouldHandleLosingBet() {
    // Arrange
    String userId = "user1";
    Bet bet =
        new Bet(
            createSessionF1(123, List.of(createDriver("driver1", "DRIVER", 3))),
            "driver2",
            100,
            BetStatus.ACCEPTED);

    // Act
    eventOutcomeServiceImpl.findWinnerAndPrize(List.of(bet), 123, "driver1", userId);

    // Assert
    assertEquals(BetStatus.LOST, bet.getBetStatus());
    verifyNoInteractions(userRepository); // No balance update occurs for losing bets
  }

  @Test
  void findDriverOdd_ShouldReturnOdds() {
    // Arrange
    List<Driver> drivers =
        List.of(createDriver("driver1", "DRIVER", 3), createDriver("driver2", "DRIVER", 2));

    // Act
    int odds = eventOutcomeServiceImpl.findDriverOdd(drivers, "driver1");

    // Assert
    assertEquals(3, odds);
  }

  @Test
  void findDriverOdd_ShouldThrowExceptionIfDriverNotFound() {
    // Arrange
    List<Driver> drivers =
        List.of(createDriver("driver1", "DRIVER", 3), createDriver("driver2", "DRIVER", 2));

    // Act and Assert
    F1BetException exception =
        assertThrows(
            F1BetException.class,
            () -> {
              eventOutcomeServiceImpl.findDriverOdd(drivers, "driver3");
            });

    assertEquals("Driver not found: driver3", exception.getMessage());
  }

  private Driver createDriver(String driverId, String fullName, int odds) {
    return Driver.builder().driverId(driverId).odds(odds).fullName(fullName).build();
  }

  private SessionF1 createSessionF1(int sessionKey, List<Driver> drivers) {
    return SessionF1.builder().sessionKey(sessionKey).drivers(drivers).build();
  }
}
