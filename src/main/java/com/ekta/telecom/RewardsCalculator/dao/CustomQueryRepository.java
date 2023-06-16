package com.ekta.telecom.RewardsCalculator.dao;

import com.ekta.telecom.RewardsCalculator.model.Transaction;

import java.time.ZonedDateTime;
import java.util.List;

public interface CustomQueryRepository {
    List<Transaction> getClientTransactionsForMonths(String clientId, ZonedDateTime fromDate);
}
