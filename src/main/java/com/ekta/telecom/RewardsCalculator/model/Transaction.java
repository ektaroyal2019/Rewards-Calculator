package com.ekta.telecom.RewardsCalculator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private long Id;
    private ZonedDateTime date;
    private String clientId;
    private BigDecimal amount;
    private String currency;
}
