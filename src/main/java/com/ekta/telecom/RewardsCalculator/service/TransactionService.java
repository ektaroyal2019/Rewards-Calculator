package com.ekta.telecom.RewardsCalculator.service;

import com.ekta.telecom.RewardsCalculator.dao.TransactionDAO;
import com.ekta.telecom.RewardsCalculator.model.Transaction;
import com.ekta.telecom.RewardsCalculator.model.TransactionRQRS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionRQRS addTransactions(TransactionRQRS transactionRQ) {
        return TransactionRQRS.builder()
                .transactions(addTransaction(transactionRQ.getTransactions())).build();
    }

    private List<Transaction> addTransaction(List<Transaction> transactions) {
        if (CollectionUtils.isEmpty(transactions)) {
            return new ArrayList<>();
        }
        List<Transaction> savedTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            savedTransactions.add(transactionDAO.save(transaction));
        }
        return savedTransactions;
    }

    public TransactionRQRS getTransaction(long transactionId) {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transactionDAO.get(transactionId));
        return TransactionRQRS.builder()
                .transactions(transactionList).build();
    }

    public TransactionRQRS deleteTransaction(long transactionId) {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transactionDAO.remove(Transaction.builder().Id(transactionId).build()));
        return TransactionRQRS.builder()
                .transactions(transactionList).build();
    }

    public TransactionRQRS updateTransaction(TransactionRQRS transactionRQ) {
        return TransactionRQRS.builder()
                .transactions(updateTransaction(transactionRQ.getTransactions())).build();
    }

    private List<Transaction> updateTransaction(List<Transaction> transactions) {
        if (CollectionUtils.isEmpty(transactions)) {
            return new ArrayList<>();
        }
        List<Transaction> savedTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            savedTransactions.add(transactionDAO.update(transaction));
        }
        return savedTransactions;
    }
}
