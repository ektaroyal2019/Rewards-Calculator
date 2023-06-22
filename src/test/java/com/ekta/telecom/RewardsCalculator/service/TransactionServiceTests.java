package com.ekta.telecom.RewardsCalculator.service;

import com.ekta.telecom.RewardsCalculator.dao.TransactionDAO;
import com.ekta.telecom.RewardsCalculator.model.Transaction;
import com.ekta.telecom.RewardsCalculator.model.TransactionRQRS;
import com.ekta.telecom.RewardsCalculator.util.TestUtility;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceTests {

    private final TransactionDAO transactionDAO = mock(TransactionDAO.class);
    private final TransactionService transactionService = new TransactionService(transactionDAO);
    private static final long NON_EXISTING_TRANSACTION_ID = 75;

    private Method addTransactionReflection() throws NoSuchMethodException {
        Method method = TransactionService.class
                .getDeclaredMethod("addTransaction", List.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    void addNullTransactionTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Transaction> nullTransaction = null;
        List<Transaction> transactionResultList = (List<Transaction>) addTransactionReflection()
                .invoke(transactionService, nullTransaction);
        assertThat(transactionResultList).isEmpty();
    }

    @Test
    void addEmptyTransactionTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Transaction> result = (List<Transaction>) addTransactionReflection()
                .invoke(transactionService, new ArrayList<>());
        assertThat(result).isEmpty();
    }

    @Test
    void addMultipleTransactionsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Transaction> multipleTransactions = TestUtility.getMultipleTransactions();
        when(transactionDAO.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        List<Transaction> transactionResultList = (List<Transaction>) addTransactionReflection()
                .invoke(transactionService, multipleTransactions);
        assertThat(transactionResultList).hasSize(4);
        assertThat(transactionResultList.get(0)).isEqualTo(multipleTransactions.get(0));
        assertThat(transactionResultList.get(1)).isEqualTo(multipleTransactions.get(1));
        assertThat(transactionResultList.get(2)).isEqualTo(multipleTransactions.get(2));
        assertThat(transactionResultList.get(3)).isEqualTo(multipleTransactions.get(3));
    }

    @Test
    void addSingleTransactionsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Transaction> singleTransaction = TestUtility.getSingleTransaction();
        when(transactionDAO.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        List<Transaction> transactionResultList = (List<Transaction>) addTransactionReflection()
                .invoke(transactionService, singleTransaction);
        assertThat(transactionResultList).hasSize(1);
        assertThat(transactionResultList.get(0)).isEqualTo(singleTransaction.get(0));
    }

    @Test
    void getTransactionTest() {
        List<Transaction> singleTransaction = TestUtility.getSingleTransaction();
        when(transactionDAO.get(anyLong())).thenAnswer(i -> singleTransaction.get(0));
        TransactionRQRS transactionResult = transactionService.getTransaction(singleTransaction.get(0).getId());
        assertThat(transactionResult.getTransactions()).hasSize(1);
        assertThat(transactionResult.getTransactions().get(0)).isEqualTo(singleTransaction.get(0));
    }

    @Test
    void getNonExistingTransactionTest() {
        when(transactionDAO.get(anyLong())).thenAnswer(i -> new Transaction());
        TransactionRQRS transactionResult = transactionService.getTransaction(NON_EXISTING_TRANSACTION_ID);
        assertThat(transactionResult.getTransactions()).hasSize(1);
        assertThat(transactionResult.getTransactions().get(0).getId()).isZero();
        assertThat(transactionResult.getTransactions().get(0).getClientId()).isNull();
    }

    @Test
    void deleteTransactionTest() {
        List<Transaction> singleTransaction = TestUtility.getSingleTransaction();
        when(transactionDAO.remove(any(Transaction.class))).thenAnswer(i -> singleTransaction.get(0));
        TransactionRQRS transactionResult = transactionService.deleteTransaction(singleTransaction.get(0).getId());
        assertThat(transactionResult.getTransactions()).hasSize(1);
        assertThat(transactionResult.getTransactions().get(0)).isEqualTo(singleTransaction.get(0));
    }

    @Test
    void deleteNullTransactionTest() {
        when(transactionDAO.remove(any(Transaction.class))).thenAnswer(i -> new Transaction());
        TransactionRQRS transactionResult = transactionService.deleteTransaction(NON_EXISTING_TRANSACTION_ID);
        assertThat(transactionResult.getTransactions()).hasSize(1);
        assertThat(transactionResult.getTransactions().get(0).getId()).isZero();
        assertThat(transactionResult.getTransactions().get(0).getClientId()).isNull();
    }

    @Test
    void deleteNonExistingTransactionTest() {
        when(transactionDAO.remove(any(Transaction.class))).thenAnswer(i -> new Transaction());
        TransactionRQRS transactionResult = transactionService.deleteTransaction(NON_EXISTING_TRANSACTION_ID);
        assertThat(transactionResult.getTransactions()).hasSize(1);
        assertThat(transactionResult.getTransactions().get(0).getId()).isZero();
        assertThat(transactionResult.getTransactions().get(0).getClientId()).isNull();
    }

    private Method updateTransactionReflection() throws NoSuchMethodException {
        Method method = TransactionService.class
                .getDeclaredMethod("updateTransaction", List.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    void updateTransactionTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Transaction> multipleTransactions = TestUtility.getMultipleTransactions();
        when(transactionDAO.update(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        List<Transaction> transactionResultList = (List<Transaction>) updateTransactionReflection()
                .invoke(transactionService, multipleTransactions);
        assertThat(transactionResultList).hasSize(4);
        assertThat(transactionResultList.get(0)).isEqualTo(multipleTransactions.get(0));
        assertThat(transactionResultList.get(1)).isEqualTo(multipleTransactions.get(1));
        assertThat(transactionResultList.get(2)).isEqualTo(multipleTransactions.get(2));
        assertThat(transactionResultList.get(3)).isEqualTo(multipleTransactions.get(3));
    }

    @Test
    void updateNullTransactionTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Transaction> nullTransaction = null;
        List<Transaction> transactionResultList = (List<Transaction>) updateTransactionReflection()
                .invoke(transactionService, nullTransaction);
        assertThat(transactionResultList).isEmpty();
    }

    @Test
    void updateEmptyTransactionTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Transaction> result = (List<Transaction>) updateTransactionReflection()
                .invoke(transactionService, new ArrayList<>());
        assertThat(result).isEmpty();
    }
}
