package com.ekta.telecom.RewardsCalculator.controller;

import com.ekta.telecom.RewardsCalculator.model.RewardRS;
import com.ekta.telecom.RewardsCalculator.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping("/{clientId}")
    @ResponseStatus(value = HttpStatus.OK)
    public RewardRS calculateClientRewards(@PathVariable final String clientId,
                                           @RequestParam(value = "noOfMonths", required = false, defaultValue = "6") long noOfMonths) {
        return rewardService.calculateClientRewards(clientId, noOfMonths);
    }
}
