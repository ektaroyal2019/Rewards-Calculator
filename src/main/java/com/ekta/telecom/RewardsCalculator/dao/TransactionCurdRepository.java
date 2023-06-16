package com.ekta.telecom.RewardsCalculator.dao;

import org.springframework.data.repository.CrudRepository;

public interface TransactionCurdRepository extends CrudRepository<TransactionDTO, Long>, CustomQueryRepository {
}
