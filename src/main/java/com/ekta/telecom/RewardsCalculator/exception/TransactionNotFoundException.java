package com.ekta.telecom.RewardsCalculator.exception;

public class TransactionNotFoundException extends RewardException {

    public TransactionNotFoundException(ErrorMessage errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public TransactionNotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}