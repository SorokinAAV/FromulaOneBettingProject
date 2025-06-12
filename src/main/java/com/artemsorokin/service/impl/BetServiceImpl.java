package com.artemsorokin.service.impl;

import com.artemsorokin.model.Bet;
import com.artemsorokin.model.BetRequest;
import com.artemsorokin.model.BetResponse;
import com.artemsorokin.model.BetStatus;
import com.artemsorokin.model.User;
import com.artemsorokin.model.UserBet;
import com.artemsorokin.repository.UserRepository;
import com.artemsorokin.service.BetService;
import com.artemsorokin.service.SessionF1Service;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BetServiceImpl implements BetService {

  private final UserRepository userRepository;
  private final SessionF1Service sessionF1Service;

  public ResponseEntity<BetResponse> placeBet(BetRequest betRequest) {
    User user = userRepository.getUser(betRequest.getUserId());

    if (user == null) {
      return ResponseEntity.status(404)
          .body(createBetResponse(betRequest, BetStatus.USER_NOT_FOUND, "User not found", null));
    }

    if (!isBetValid(user, betRequest.getAmount())) {
      return ResponseEntity.status(400)
          .body(
              createBetResponse(
                  betRequest, BetStatus.INVALID_BET_AMOUNT, "Insufficient balance!", null));
    }

    var sessionF1 = sessionF1Service.getSessionByKey(betRequest.getSessionKey());

    if (sessionF1 == null || sessionF1.isEmpty() || sessionF1.getFirst() == null) {
      return ResponseEntity.status(404)
          .body(
              createBetResponse(
                  betRequest, BetStatus.INVALID_SESSION_KEY, "Session not found", null));
    }
    // TODO check if driver exists in session

    // Deduct balance and store the bet
    userRepository.updateUserBalance(
        betRequest.getUserId(), user.getBalance() - betRequest.getAmount());
    userRepository.addBet(
        betRequest.getUserId(),
        new Bet(
            sessionF1.getFirst(),
            betRequest.getDriverId(),
            betRequest.getAmount(),
            BetStatus.ACCEPTED));

    return ResponseEntity.status(200)
        .body(
            createBetResponse(
                betRequest, BetStatus.ACCEPTED, "Bet placed successfully!", user.getBalance()));
  }

  private boolean isBetValid(User user, double amount) {
    var userBalance = user.getBalance();
    return userBalance >= amount && amount > 0 && userBalance - amount >= 0;
  }

  public ResponseEntity<UserBet> getUserBets(String userId) {
    var user = userRepository.getUser(userId);
    var userBet =
        UserBet.builder()
            .userId(userId)
            .bets(userRepository.getUserBets(userId))
            .totalBalance(user.getBalance())
            .build();

    return ResponseEntity.status(200).body(userBet);
  }

  private BetResponse createBetResponse(
      BetRequest betRequest, BetStatus betStatus, String message, Double balance) {
    return BetResponse.builder()
        .userId(betRequest.getUserId())
        .sessionKey(betRequest.getSessionKey())
        .driverId(betRequest.getDriverId())
        .amount(betRequest.getAmount())
        .balance(balance)
        .status(betStatus)
        .message(message)
        .build();
  }
}
