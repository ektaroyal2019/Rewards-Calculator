package com.ekta.telecom.RewardsCalculator.controller;

import com.ekta.telecom.RewardsCalculator.handler.TransactionHandler;
import com.ekta.telecom.RewardsCalculator.model.TransactionRQRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionHandler transactionHandler;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionRQRS addTransactions(@RequestBody TransactionRQRS transactionRQ) {
        return transactionHandler.addTransactions(transactionRQ);
    }

    @GetMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionRQRS getTransactions(@PathVariable final long transactionId) {
        return transactionHandler.getTransaction(transactionId);
    }

    @DeleteMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionRQRS deleteTransactions(@PathVariable final long transactionId) {
        return transactionHandler.deleteTransactions(transactionId);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public TransactionRQRS updateTransaction(@RequestBody TransactionRQRS transactionRQ) {
        return transactionHandler.updateTransaction(transactionRQ);
    }
}
