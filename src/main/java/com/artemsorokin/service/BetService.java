package com.artemsorokin.service;

import com.artemsorokin.model.BetRequest;
import com.artemsorokin.model.BetResponse;
import com.artemsorokin.model.SessionF1;
import com.artemsorokin.model.UserBet;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BetService {

  public ResponseEntity<BetResponse> placeBet(BetRequest betRequest);

  public ResponseEntity<UserBet> getUserBets(String userId);
}
