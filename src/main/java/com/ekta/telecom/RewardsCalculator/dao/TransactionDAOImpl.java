package com.ekta.telecom.RewardsCalculator.dao;

import com.ekta.telecom.RewardsCalculator.exception.ErrorMessage;
import com.ekta.telecom.RewardsCalculator.exception.TransactionNotFoundException;
import com.ekta.telecom.RewardsCalculator.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionDAOImpl implements TransactionDAO {

    private final TransactionCurdRepository repository;

    @Override
    public List<Transaction> getClientTransactionsForMonths(String clientId, ZonedDateTime fromDate) {
        return repository.getClientTransactionsForMonths(clientId, fromDate);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return mapTransaction(repository.save(TransactionDTO.of(transaction)));
    }

    @Override
    public Transaction update(Transaction transaction) {
        TransactionDTO model = repository.findById(transaction.getId()).orElseThrow(() -> new TransactionNotFoundException(ErrorMessage.TRANSACTION_NOT_FOUND));
        model.setTransactionDate(transaction.getDate());
        model.setCurrency(transaction.getCurrency());
        model.setAmount(transaction.getAmount());
        return mapTransaction(repository.save(model));
    }

    @Override
    public Transaction remove(Transaction transaction) {
        Transaction fetchedTransaction = get(transaction.getId());
        repository.deleteById(transaction.getId());
        return fetchedTransaction;
    }

    @Override
    public Transaction get(long Id) {
        return mapTransaction(repository.findById(Id).orElseThrow(() -> new TransactionNotFoundException(ErrorMessage.TRANSACTION_NOT_FOUND)));
    }

    private Transaction mapTransaction(TransactionDTO transactionDTO) {
        return new Transaction(transactionDTO.getId(), transactionDTO.getTransactionDate(),
                transactionDTO.getClientId(), transactionDTO.getAmount(), transactionDTO.getCurrency());
    }
}
