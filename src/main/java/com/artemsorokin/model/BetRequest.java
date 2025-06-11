package com.artemsorokin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BetRequest {

  private String userId;
  private int sessionKey;
  private String driverId;
  private double amount;
}
