package com.ekta.telecom.RewardsCalculator.exception;

public class RequestValidationErrorException extends RewardException {
    public RequestValidationErrorException(final ErrorMessage errorMessage, final String additionalMessage) {
        super(errorMessage, additionalMessage);
    }
}
