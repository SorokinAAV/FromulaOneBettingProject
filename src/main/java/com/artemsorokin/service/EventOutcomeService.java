package com.artemsorokin.service;

import com.artemsorokin.exception.F1BetException;
import com.artemsorokin.model.Bet;
import com.artemsorokin.model.BetStatus;
import com.artemsorokin.model.Driver;
import com.artemsorokin.model.EventOutcome;
import com.artemsorokin.model.User;
import com.artemsorokin.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventOutcomeService {

  private final UserRepository userRepository;

  public String processEventOutcome(EventOutcome eventOutcome) {
    var bets = userRepository.getAllBets();
    bets.forEach(
        (key, value) ->
            findWinnerAndPrize(
                value, eventOutcome.getSessionKey(), eventOutcome.getWinningDriverId(), key));

    return "Event outcome processed successfully!";
  }

  void findWinnerAndPrize(
      List<Bet> bets, int sessionKey, String winningDriverId, String userId) {
    List<Bet> matchingBets = new ArrayList<>();
    for (Bet bet : bets) {
      if (sessionKey == bet.getSessionF1().getSessionKey()) {
        matchingBets.add(bet);
      }
    }

    for (Bet bet : matchingBets) {
      if (winningDriverId.equalsIgnoreCase(bet.getDriverId())) {
        bet.setBetStatus(BetStatus.WON);
        double prize =
            bet.getAmount() * findDriverOdd(bet.getSessionF1().getDrivers(), winningDriverId);

        // Update user's balance
        User user = userRepository.getUser(userId);
        if (user != null) {
          userRepository.updateUserBalance(user.getUserId(), user.getBalance() + prize);
        }
      } else {
        bet.setBetStatus(BetStatus.LOST);
      }
    }
  }

  int findDriverOdd(List<Driver> drivers, String driverId) {
    return drivers.stream()
        .filter(driver -> driver.getDriverId().equals(driverId))
        .findFirst()
        .map(Driver::getOdds)
        .orElseThrow(() -> new F1BetException("Driver not found: " + driverId));
  }
}
