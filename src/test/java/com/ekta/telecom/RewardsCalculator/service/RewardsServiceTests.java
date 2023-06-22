package com.ekta.telecom.RewardsCalculator.service;

import com.ekta.telecom.RewardsCalculator.dao.TransactionDAO;
import com.ekta.telecom.RewardsCalculator.exception.ErrorMessage;
import com.ekta.telecom.RewardsCalculator.model.Reward;
import com.ekta.telecom.RewardsCalculator.model.RewardRS;
import com.ekta.telecom.RewardsCalculator.model.Transaction;
import com.ekta.telecom.RewardsCalculator.util.TestUtility;
import com.ekta.telecom.RewardsCalculator.utility.RequestResponseValidator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RewardsServiceTests {

    private static final String CLIENT_ID = "CLIENT1";
    private static final int NO_OF_MONTHS = 6;
    private final TransactionDAO transactionDAO = mock(TransactionDAO.class);
    private final RewardService rewardService = new RewardService(transactionDAO);

    @Test
    void calculateClientRewardsTest() {
        List<Transaction> multipleTransactions = TestUtility.getMultipleTransactions();
        when(transactionDAO.getClientTransactionsForMonths(anyString(), any())).thenReturn(multipleTransactions);
        RewardRS rewardRS = rewardService.calculateClientRewards(CLIENT_ID, NO_OF_MONTHS);
        assertThat(rewardRS.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(rewardRS.getMonthlyRewardPoints()).hasSize(2);
        assertThat(rewardRS.getTotalRewardPoints()).isEqualTo(548);
        assertThat(rewardRS.getMonthlyRewardPoints().get("April")).isNull();
        assertThat(rewardRS.getMonthlyRewardPoints().get("May")).isEqualTo(324);
        assertThat(rewardRS.getMonthlyRewardPoints().get("June")).isEqualTo(224);
        assertThat(rewardRS.getMonthlyRewardPoints().get("July")).isNull();
    }

    @Test
    void calculateClientRewardsNonExistingTransactionTest() {
        when(transactionDAO.getClientTransactionsForMonths(anyString(), any())).thenReturn(new ArrayList<>());
        RewardRS rewardRS = rewardService.calculateClientRewards(CLIENT_ID, NO_OF_MONTHS);
        assertThat(rewardRS.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(rewardRS.getMonthlyRewardPoints()).isEmpty();
        assertThat(rewardRS.getTotalRewardPoints()).isZero();
    }

    @Test
    void calculateClientRewardsWithNegativeTransactionAmountTest() {
        List<Transaction> negativeAmountTransaction = TestUtility.getNegativeAmountTransaction();
        when(transactionDAO.getClientTransactionsForMonths(anyString(), any())).thenReturn(negativeAmountTransaction);
        RewardRS rewardRS = rewardService.calculateClientRewards(CLIENT_ID, NO_OF_MONTHS);
        assertThat(rewardRS.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(rewardRS.getMonthlyRewardPoints()).hasSize(1);
        assertThat(rewardRS.getTotalRewardPoints()).isZero();
        assertThat(rewardRS.getMonthlyRewardPoints().get("April")).isNull();
        assertThat(rewardRS.getMonthlyRewardPoints().get("May")).isNull();
        assertThat(rewardRS.getMonthlyRewardPoints().get("June")).isZero();
        assertThat(rewardRS.getMonthlyRewardPoints().get("July")).isNull();
    }

    @Test
    void calculateClientRewardsWithMissingTransactionDateTest() {
        List<Transaction> missingDateTransaction = TestUtility.getSingleTransaction();
        missingDateTransaction.get(0).setDate(null);
        when(transactionDAO.getClientTransactionsForMonths(anyString(), any())).thenReturn(missingDateTransaction);
        Throwable throwable = assertThrows(Throwable.class, () -> rewardService.calculateClientRewards(CLIENT_ID, NO_OF_MONTHS));
        assertThat(throwable.getMessage()).isEqualTo("Cannot invoke \"java.time.ZonedDateTime.getMonth()\" because the return value of \"com.ekta.telecom.RewardsCalculator.model.Transaction.getDate()\" is null");
    }

    @Test
    void calculateClientRewardsWithMissingTransactionAmountTest() {
        List<Transaction> missingAmountTransaction = TestUtility.getSingleTransaction();
        missingAmountTransaction.get(0).setAmount(null);
        when(transactionDAO.getClientTransactionsForMonths(anyString(), any())).thenReturn(missingAmountTransaction);
        Throwable throwable = assertThrows(Throwable.class, () -> rewardService.calculateClientRewards(CLIENT_ID, NO_OF_MONTHS));
        assertThat(throwable.getMessage()).isEqualTo("Cannot invoke \"java.math.BigDecimal.compareTo(java.math.BigDecimal)\" because \"transactionAmount\" is null");
    }

    @Test
    void calculateClientRewardsWithMissingTransactionCurrencyTest() {
        List<Transaction> missingCurrencyTransaction = TestUtility.getSingleTransaction();
        missingCurrencyTransaction.get(0).setCurrency(null);
        when(transactionDAO.getClientTransactionsForMonths(anyString(), any())).thenReturn(missingCurrencyTransaction);
        RewardRS rewardRS = rewardService.calculateClientRewards(CLIENT_ID, NO_OF_MONTHS);
        assertThat(rewardRS.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(rewardRS.getMonthlyRewardPoints()).hasSize(1);
        assertThat(rewardRS.getTotalRewardPoints()).isEqualTo(124);
        assertThat(rewardRS.getMonthlyRewardPoints().get("April")).isNull();
        assertThat(rewardRS.getMonthlyRewardPoints().get("May")).isNull();
        assertThat(rewardRS.getMonthlyRewardPoints().get("June")).isEqualTo(124);
        assertThat(rewardRS.getMonthlyRewardPoints().get("July")).isNull();
    }

    private Method calculateMonthlyRewardsReflection() throws NoSuchMethodException {
        Method method = RewardService.class
                .getDeclaredMethod("calculateMonthlyRewards", List.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    void calculateMonthlyRewardsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Transaction> multipleTransactions = TestUtility.getMultipleTransactions();
        List<Reward> rewards = TestUtility.getMultipleRewards();
        List<Reward> rewardResultList = (List<Reward>) calculateMonthlyRewardsReflection()
                .invoke(rewardService, multipleTransactions);
        assertThat(rewardResultList).hasSize(4);
        assertThat(rewardResultList.get(0)).isEqualTo(rewards.get(0));
        assertThat(rewardResultList.get(1)).isEqualTo(rewards.get(1));
        assertThat(rewardResultList.get(2)).isEqualTo(rewards.get(2));
        assertThat(rewardResultList.get(3)).isEqualTo(rewards.get(3));
    }
}
