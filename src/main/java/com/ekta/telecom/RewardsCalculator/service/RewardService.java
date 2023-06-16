package com.ekta.telecom.RewardsCalculator.service;

import com.ekta.telecom.RewardsCalculator.dao.TransactionDAO;
import com.ekta.telecom.RewardsCalculator.model.Reward;
import com.ekta.telecom.RewardsCalculator.model.RewardRS;
import com.ekta.telecom.RewardsCalculator.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardService {
    private static final int FIRST_THRESHOLD_LEVEL = 50;
    private static final int THRESHOLD_LEVEL_SECOND = 100;
    private static final int FIRST_THRESHOLD_LEVEL_POINTS_MULTIPLIER = 1;
    private static final int SECOND_THRESHOLD_LEVEL_POINTS_MULTIPLIER = 2;
    private final TransactionDAO transactionDAO;

    private List<Reward> calculateMonthlyRewards(List<Transaction> transactions) {
        List<Reward> rewardList = new ArrayList<>();
        if (transactions.isEmpty()) {
            return rewardList;
        }
        for (Transaction transaction : transactions) {
            long rewardPoints = 0L;
            Double transactionAmount = transaction.getAmount();
            if (transactionAmount > THRESHOLD_LEVEL_SECOND) {
                long valueAboveThreshold = (long) (transactionAmount - THRESHOLD_LEVEL_SECOND);
                rewardPoints += SECOND_THRESHOLD_LEVEL_POINTS_MULTIPLIER * valueAboveThreshold + FIRST_THRESHOLD_LEVEL * FIRST_THRESHOLD_LEVEL_POINTS_MULTIPLIER;
            } else if (transactionAmount > FIRST_THRESHOLD_LEVEL) {
                long valueAboveThreshold = (long) (transactionAmount - FIRST_THRESHOLD_LEVEL);
                rewardPoints += FIRST_THRESHOLD_LEVEL_POINTS_MULTIPLIER * valueAboveThreshold;
            }
            rewardList.add(new Reward(rewardPoints, transaction.getId()));
        }
        return rewardList;
    }

    public RewardRS calculateClientRewards(String clientId, long noOfMonths) {
        List<Transaction> transactions = transactionDAO.getClientTransactionsForMonths(clientId, ZonedDateTime.now(ZoneOffset.UTC).minusMonths(noOfMonths));
        Map<String, List<Transaction>> transactionsByMonths = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.US), Collectors.toList()));
        Map<String, List<Reward>> monthlyRewards = transactionsByMonths.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, v -> calculateMonthlyRewards(v.getValue())));
        long totalValue = monthlyRewards.values().stream().flatMap(Collection::stream).mapToLong(Reward::getValue).sum();
        return RewardRS.builder()
                .clientId(clientId)
                .totalRewardAmount(totalValue)
                .monthlyRewardAmount(monthlyRewards.entrySet().stream().collect(
                        Collectors.toMap(Map.Entry::getKey, v -> v.getValue().stream().mapToLong(Reward::getValue).sum())))
                .build();

    }
}
