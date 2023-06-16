package com.ekta.telecom.RewardsCalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reward {
    private long value;
    private long transactionId;
}
