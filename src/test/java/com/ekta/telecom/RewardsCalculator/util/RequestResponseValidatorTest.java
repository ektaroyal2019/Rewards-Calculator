package com.ekta.telecom.RewardsCalculator.util;

import com.ekta.telecom.RewardsCalculator.exception.ErrorMessage;
import com.ekta.telecom.RewardsCalculator.model.Transaction;
import com.ekta.telecom.RewardsCalculator.model.TransactionRQRS;
import com.ekta.telecom.RewardsCalculator.utility.RequestResponseValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestResponseValidatorTest {

    List<Transaction> singleTransaction = TestUtility.getSingleTransaction();

    @Test
    void validateTransactionMissingAmountTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setAmount(null);
        Throwable throwable = assertThrows(Throwable.class, () -> RequestResponseValidator.validate(request));
        assertThat(throwable.getMessage()).isEqualTo("Transaction amount is missing\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
    }

    @Test
    void validateTransactionMissingCurrencyTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setCurrency(null);
        Throwable throwable = assertThrows(Throwable.class, () -> RequestResponseValidator.validate(request));
        assertThat(throwable.getMessage()).isEqualTo("Transaction currency is missing\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
    }

    @Test
    void validateTransactionInvalidCurrencyTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setCurrency("invalidCurrency");
        Throwable throwable = assertThrows(Throwable.class, () -> RequestResponseValidator.validate(request));
        assertThat(throwable.getMessage()).isEqualTo("Invalid transaction currency\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
    }

    @Test
    void validateTransactionWithoutDateTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setDate(null);
        Throwable throwable = assertThrows(Throwable.class, () -> RequestResponseValidator.validate(request));
        assertThat(throwable.getMessage()).isEqualTo("Transaction date is missing\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
    }

    @Test
    void validateTransactionWithEmptyTransactionTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(new ArrayList<>());
        Throwable throwable = assertThrows(Throwable.class, () -> RequestResponseValidator.validate(request));
        assertThat(throwable.getMessage()).isEqualTo("Transactions cannot be null\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
    }

    @Test
    void validateTransactionsWithNegativeAmountTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(TestUtility.getNegativeAmountTransaction());
        Throwable throwable = assertThrows(Throwable.class, () -> RequestResponseValidator.validate(request));
        assertThat(throwable.getMessage()).isEqualTo("Invalid transaction amount\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
    }
}
