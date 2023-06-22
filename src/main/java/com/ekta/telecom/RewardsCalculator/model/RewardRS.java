package com.ekta.telecom.RewardsCalculator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardRS {
    private String clientId;
    private long totalRewardPoints;
    private Map<String, Long> monthlyRewardPoints;
}
