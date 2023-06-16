package com.ekta.telecom.RewardsCalculator.exception;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;

@Getter
public class RewardException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final String additionalErrorMessage;

    protected RewardException(ErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());
        this.errorMessage = errorMessage;
        additionalErrorMessage = null;
    }

    protected RewardException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getErrorMessage(), cause);
        this.errorMessage = errorMessage;
        additionalErrorMessage = null;
    }

    protected RewardException(ErrorMessage errorMessage, String additionalErrorMessage) {
        super(errorMessage.getErrorMessage());
        this.errorMessage = errorMessage;
        this.additionalErrorMessage = additionalErrorMessage;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        if (StringUtils.isNotEmpty(additionalErrorMessage)) {
            message.append(additionalErrorMessage).append(System.lineSeparator());
        }
        message.append(super.getMessage());
        return message.toString();
    }
}