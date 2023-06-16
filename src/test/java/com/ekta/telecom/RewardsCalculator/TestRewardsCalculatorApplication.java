package com.ekta.telecom.RewardsCalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
class TestRewardsCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.from(RewardsCalculatorApplication::main).with(TestRewardsCalculatorApplication.class).run(args);
	}

}
