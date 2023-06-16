package com.ekta.telecom.RewardsCalculator.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    CREATE_TRANSACTION_VALIDATION_ERROR("4000", "Create transaction request validation error"),
    TRANSACTION_NOT_FOUND("4004", "Transaction not found");

    private final String errorCode;
    private final String errorMessage;
}
