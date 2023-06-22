package com.ekta.telecom.RewardsCalculator.dao;

import com.ekta.telecom.RewardsCalculator.model.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "TRANSACTION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime transactionDate;

    @Column
    private String clientId;

    @Column
    private BigDecimal amount;

    @Column
    private String currency;

    public static TransactionDTO of(Transaction transaction) {
        return TransactionDTO.builder()
                .transactionDate(transaction.getDate())
                .clientId(transaction.getClientId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .build();
    }
}
