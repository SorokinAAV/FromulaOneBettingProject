package com.artemsorokin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bet {

  private SessionF1 sessionF1;
  private String driverId;
  private double amount;
  private BetStatus betStatus;
}
