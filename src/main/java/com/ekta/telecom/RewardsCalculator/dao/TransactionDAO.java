package com.ekta.telecom.RewardsCalculator.dao;

import com.ekta.telecom.RewardsCalculator.model.Transaction;

import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionDAO {
    Transaction save(Transaction transaction);

    Transaction update(Transaction transaction);

    Transaction remove(Transaction transaction);

    Transaction get(long Id);

    List<Transaction> getClientTransactionsForMonths(String clientId, ZonedDateTime from);
}
