package com.ekta.telecom.RewardsCalculator.handler;

import com.ekta.telecom.RewardsCalculator.model.TransactionRQRS;
import com.ekta.telecom.RewardsCalculator.service.TransactionService;
import com.ekta.telecom.RewardsCalculator.utility.RequestResponseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionHandler {

    private final TransactionService transactionService;

    public TransactionRQRS addTransactions(TransactionRQRS transactionRQ) {
        RequestResponseValidator.validate(transactionRQ);
        return transactionService.addTransactions(transactionRQ);
    }

    public TransactionRQRS getTransaction(long transactionId) {
        return transactionService.getTransaction(transactionId);
    }

    public TransactionRQRS deleteTransactions(long transactionId) {
        return transactionService.deleteTransaction(transactionId);
    }

    public TransactionRQRS updateTransaction(TransactionRQRS transactionRQ) {
        RequestResponseValidator.validate(transactionRQ);
        return transactionService.updateTransaction(transactionRQ);
    }
}
