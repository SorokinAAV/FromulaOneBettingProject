package com.artemsorokin.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetResponse {
    private String userId;
    private int sessionKey;
    private String driverId;
    private double amount;
    private BetStatus status;
    private String message;
    private Double balance;
}
