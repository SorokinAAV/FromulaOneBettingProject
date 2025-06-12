package com.artemsorokin.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.artemsorokin.model.*;
import com.artemsorokin.repository.impl.UserRepositoryImpl;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class BetServiceImplTest {

    @Mock
    private UserRepositoryImpl userRepositoryImpl;

    @Mock
    private SessionF1ServiceImpl sessionF1ServiceImpl;

    @InjectMocks
    private BetServiceImpl betServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void placeBet_UserNotFound() {
        // Arrange
        BetRequest betRequest = new BetRequest("user1", 123, "driver1", 100.0);
        when(userRepositoryImpl.getUser(anyString())).thenReturn(null);

        // Act
        ResponseEntity<BetResponse> response = betServiceImpl.placeBet(betRequest);

        // Assert
        assertEquals(404, response.getStatusCode().value());
        assertEquals(BetStatus.USER_NOT_FOUND, response.getBody().getStatus());
        assertEquals("User not found", response.getBody().getMessage());
        verify(userRepositoryImpl, never()).updateUserBalance(anyString(), anyDouble());
        verify(userRepositoryImpl, never()).addBet(anyString(), any());
    }

    @Test
    void placeBet_InvalidBetAmount() {
        // Arrange
        BetRequest betRequest = new BetRequest("user1", 123, "driver1", 100.0);
        User user = new User("user1", 50.0);
        when(userRepositoryImpl.getUser("user1")).thenReturn(user);

        // Act
        ResponseEntity<BetResponse> response = betServiceImpl.placeBet(betRequest);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals(BetStatus.INVALID_BET_AMOUNT, response.getBody().getStatus());
        assertEquals("Insufficient balance!", response.getBody().getMessage());
        verify(userRepositoryImpl, never()).updateUserBalance(anyString(), anyDouble());
        verify(userRepositoryImpl, never()).addBet(anyString(), any());
    }

    @Test
    void placeBet_InvalidSessionKey() {
        // Arrange
        BetRequest betRequest = new BetRequest("user1", 123, "driver1", 50.0);
        User user = new User("user1", 100.0);
        when(userRepositoryImpl.getUser("user1")).thenReturn(user);
        when(sessionF1ServiceImpl.getSessionByKey(123)).thenReturn(null);

        // Act
        ResponseEntity<BetResponse> response = betServiceImpl.placeBet(betRequest);

        // Assert
        assertEquals(404, response.getStatusCode().value());
        assertEquals(BetStatus.INVALID_SESSION_KEY, response.getBody().getStatus());
        assertEquals("Session not found", response.getBody().getMessage());
        verify(userRepositoryImpl, never()).updateUserBalance(anyString(), anyDouble());
        verify(userRepositoryImpl, never()).addBet(anyString(), any());
    }

    @Test
    void placeBet_Successful() {
        // Arrange
        BetRequest betRequest = new BetRequest("user1", 123, "driver1", 50.0);
        User user = new User("user1", 100.0);
        SessionF1 sessionF1 = SessionF1.builder().build();

        when(userRepositoryImpl.getUser("user1")).thenReturn(user);
        when(sessionF1ServiceImpl.getSessionByKey(123)).thenReturn(List.of(sessionF1));

        // Act
        ResponseEntity<BetResponse> response = betServiceImpl.placeBet(betRequest);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(BetStatus.ACCEPTED, response.getBody().getStatus());
        assertEquals("Bet placed successfully!", response.getBody().getMessage());
        verify(userRepositoryImpl).updateUserBalance("user1", 50.0); // Deduct balance
        verify(userRepositoryImpl).addBet(eq("user1"), any(Bet.class));
    }

    @Test
    void getUserBets_Successful() {
        // Arrange
        String userId = "user1";
        User user = new User(userId, 100.0);
        Bet bet1 = new Bet(SessionF1.builder().build(), "driver1", 50.0, BetStatus.ACCEPTED);
        Bet bet2 = new Bet(SessionF1.builder().build(), "driver2", 30.0, BetStatus.ACCEPTED);

        when(userRepositoryImpl.getUser(userId)).thenReturn(user);
        when(userRepositoryImpl.getUserBets(userId)).thenReturn(List.of(bet1, bet2));

        // Act
        ResponseEntity<UserBet> response = betServiceImpl.getUserBets(userId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(userId, response.getBody().getUserId());
        assertEquals(2, response.getBody().getBets().size());
        assertEquals(100.0, response.getBody().getTotalBalance());
        verify(userRepositoryImpl).getUser(userId);
        verify(userRepositoryImpl).getUserBets(userId);
    }
}
