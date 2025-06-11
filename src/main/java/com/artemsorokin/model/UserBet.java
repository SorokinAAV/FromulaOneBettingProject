package com.artemsorokin.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserBet {

    private String userId;
    private List<Bet> bets;
    private double totalBalance;
}
