package com.artemsorokin.controller;

import com.artemsorokin.model.BetRequest;
import com.artemsorokin.model.BetResponse;
import com.artemsorokin.model.UserBet;
import com.artemsorokin.service.BetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/f1")
@AllArgsConstructor
public class BetController {

  private final BetService betService;

  @PostMapping("/bet")
  public ResponseEntity<BetResponse> placeBet(@RequestBody BetRequest betRequest) {
    return betService.placeBet(betRequest);
  }

  @GetMapping("/bets")
  public ResponseEntity<UserBet> getUserBets(@RequestParam String userId) {
    return betService.getUserBets(userId);
  }
}
