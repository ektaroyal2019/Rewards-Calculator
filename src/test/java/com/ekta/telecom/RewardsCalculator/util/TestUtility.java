package com.ekta.telecom.RewardsCalculator.util;

import com.ekta.telecom.RewardsCalculator.model.Reward;
import com.ekta.telecom.RewardsCalculator.model.Transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtility {

    private static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal(100);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(137.5);
    private static final String TRANSACTION_CURRENCY = "USD";
    private static final String CLIENT_ID = "CLIENT1";
    private static final ZonedDateTime TRANSACTION_DATE = ZonedDateTime.parse("2023-06-13T17:15:45.111Z");

    public static List<Transaction> getMultipleTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, TRANSACTION_DATE.plusDays(7), CLIENT_ID, TRANSACTION_AMOUNT, TRANSACTION_CURRENCY));
        transactions.add(new Transaction(2, TRANSACTION_DATE, CLIENT_ID, TRANSACTION_AMOUNT, TRANSACTION_CURRENCY));
        transactions.add(new Transaction(3, TRANSACTION_DATE.minusMonths(1).minusDays(7), CLIENT_ID, TRANSACTION_AMOUNT.add(UPDATED_AMOUNT), TRANSACTION_CURRENCY));
        transactions.add(new Transaction(4, TRANSACTION_DATE.minusWeeks(1), CLIENT_ID, UPDATED_AMOUNT, TRANSACTION_CURRENCY));
        return transactions;
    }

    public static List<Transaction> getNegativeAmountTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, TRANSACTION_DATE.plusDays(7), CLIENT_ID, TRANSACTION_AMOUNT.negate(), TRANSACTION_CURRENCY));
        return transactions;
    }

    public static List<Transaction> getSingleTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, TRANSACTION_DATE.plusDays(2), CLIENT_ID, UPDATED_AMOUNT, TRANSACTION_CURRENCY));
        return transactions;
    }

    public static List<Reward> getMultipleRewards() {
        List<Reward> rewards = new ArrayList<>();
        rewards.add(new Reward(50,1));
        rewards.add(new Reward(50,2));
        rewards.add(new Reward(324,3));
        rewards.add(new Reward(124,4));
        return rewards;
    }
}
