package com.artemsorokin.service;

import com.artemsorokin.model.BetRequest;
import com.artemsorokin.model.BetResponse;
import com.artemsorokin.model.UserBet;
import org.springframework.http.ResponseEntity;


public interface BetService {

  ResponseEntity<BetResponse> placeBet(BetRequest betRequest);

  ResponseEntity<UserBet> getUserBets(String userId);
}
