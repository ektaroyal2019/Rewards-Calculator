package com.ekta.telecom.RewardsCalculator.util;

import com.ekta.telecom.RewardsCalculator.model.Transaction;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class testUtility {

    private static final double TRANSACTION_AMOUNT = 100.0;
    private static final double UPDATED_AMOUNT = 137.5;
    private static final String TRANSACTION_CURRENCY = "USD";
    private static final String CLIENT_ID = "CLIENT1";
    private static final ZonedDateTime TRANSACTION_DATE = ZonedDateTime.parse("2023-06-13T17:15:45.111Z");

    public static List<Transaction> getMultipleTransactions() {
        //given
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, TRANSACTION_DATE.plusDays(7), CLIENT_ID, TRANSACTION_AMOUNT, TRANSACTION_CURRENCY));
        transactions.add(new Transaction(2, TRANSACTION_DATE, CLIENT_ID, TRANSACTION_AMOUNT, TRANSACTION_CURRENCY));
        transactions.add(new Transaction(3, TRANSACTION_DATE.minusMonths(1).minusDays(7), CLIENT_ID, TRANSACTION_AMOUNT, TRANSACTION_CURRENCY));
        transactions.add(new Transaction(4, TRANSACTION_DATE.minusWeeks(1), CLIENT_ID, TRANSACTION_AMOUNT, TRANSACTION_CURRENCY));
        return transactions;
    }

    public static List<Transaction> getSingleTransaction() {
        //given
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, TRANSACTION_DATE.plusDays(2), CLIENT_ID, UPDATED_AMOUNT, TRANSACTION_CURRENCY));
        return transactions;
    }
}
