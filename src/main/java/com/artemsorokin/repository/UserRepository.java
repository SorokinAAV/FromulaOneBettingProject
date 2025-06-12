package com.artemsorokin.repository;

import com.artemsorokin.model.Bet;
import com.artemsorokin.model.User;
import java.util.List;
import java.util.Map;

public interface UserRepository {

  User getUser(String userId);

  void updateUserBalance(String userId, double newBalance);

  void addBet(String userId, Bet bet);

  List<Bet> getUserBets(String userId);

  Map<String, List<Bet>> getAllBets();
}
