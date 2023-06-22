package com.ekta.telecom.RewardsCalculator.dao;

import com.ekta.telecom.RewardsCalculator.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CustomQueryRepositoryImpl implements CustomQueryRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Transaction> getClientTransactionsForMonths(String clientId, ZonedDateTime fromDate) {
        TypedQuery<TransactionDTO> query = entityManager.createQuery("SELECT t FROM  TransactionDTO t where t.clientId = :clientId and t.transactionDate > :fromDate", TransactionDTO.class);
        query.setParameter("clientId", clientId);
        query.setParameter("fromDate", fromDate);
        List<TransactionDTO> transactionDTOs = query.getResultList();
        return transactionDTOs.stream()
                .map(t -> new Transaction(
                        t.getId(), t.getTransactionDate(), t.getClientId(), t.getAmount(), t.getCurrency())
                ).toList();
    }
}
