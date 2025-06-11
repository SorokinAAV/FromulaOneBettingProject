package com.artemsorokin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventOutcome {

  private int sessionKey;
  private String winningDriverId;
}
