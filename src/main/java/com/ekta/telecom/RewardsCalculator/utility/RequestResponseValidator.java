package com.ekta.telecom.RewardsCalculator.utility;

import com.ekta.telecom.RewardsCalculator.exception.ErrorMessage;
import com.ekta.telecom.RewardsCalculator.exception.RequestValidationErrorException;
import com.ekta.telecom.RewardsCalculator.model.Transaction;
import com.ekta.telecom.RewardsCalculator.model.TransactionRQRS;
import io.micrometer.common.util.StringUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Currency;


public class RequestResponseValidator {

    public static void validate(TransactionRQRS transactionRQ) {
        if (CollectionUtils.isEmpty(transactionRQ.getTransactions())) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Transactions cannot be null");
        }
        transactionRQ.getTransactions().forEach(transaction -> validateClientTransaction(transaction));
    }

    private static void validateClientTransaction(Transaction transaction) {
        if (transaction.getDate() == null) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Transaction date is missing");
        }
        if (transaction.getAmount() == null) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Transaction amount is missing");
        }
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Invalid transaction amount");
        }

        if (StringUtils.isBlank(transaction.getCurrency())) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Transaction currency is missing");
        }
        try {
            Currency.getInstance(transaction.getCurrency());
        } catch (IllegalArgumentException e) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Invalid transaction currency");
        }
    }
}
