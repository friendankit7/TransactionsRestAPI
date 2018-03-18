package com.transactions.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transactions.model.Transaction;

/**
 * Business logic for computing transactions
 *
 * @author ankitgoyal
 */
@Service
public class TransactionServiceImpl implements TransactionService {

	private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	public static final int TIME_LIMIT = 60;

	@Autowired
	private StatisticsService statisticsService;

	@Override
	public void addTransaction(Transaction transaction) {
		logger.info("New transaction", transaction);
		statisticsService.computeStatistics(transaction);
	}
}
