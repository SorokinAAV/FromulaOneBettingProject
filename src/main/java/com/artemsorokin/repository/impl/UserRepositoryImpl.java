package com.artemsorokin.repository.impl;

import com.artemsorokin.model.Bet;
import com.artemsorokin.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artemsorokin.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> userStore = new HashMap<>();
    private final Map<String, List<Bet>> betStore = new HashMap<>();

    public UserRepositoryImpl() {
        // Simulate user registration: assign each user 100 EUR balance
        userStore.put("123", new User("123", 100.0));
        userStore.put("456", new User("456", 100.0));
        userStore.put("789", new User("789", 100.0));
    }

    public User getUser(String userId) {
        return userStore.get(userId);
    }

    public void updateUserBalance(String userId, double newBalance) {
        User user = userStore.get(userId);
        if (user != null) {
            user.setBalance(newBalance);
        }
    }

    public void addBet(String userId, Bet bet) {
        betStore.computeIfAbsent(userId, k -> new java.util.ArrayList<>()).add(bet);
    }

    public List<Bet> getUserBets(String userId) {
        return betStore.getOrDefault(userId, new java.util.ArrayList<>());
    }

    public Map<String, List<Bet>> getAllBets() {
        return betStore;
    }
}
