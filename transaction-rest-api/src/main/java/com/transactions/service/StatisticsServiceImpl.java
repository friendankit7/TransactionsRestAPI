package com.transactions.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.transactions.common.APIErrors;
import com.transactions.exception.RequestValidationException;
import com.transactions.model.ComputedStatistics;
import com.transactions.model.Statistics;
import com.transactions.model.Transaction;

/**
 * Business Logic for Computing and Presenting Statistics
 *
 * @author ankitgoyal
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

	private static final int DURATION_STATS = 60;
	private static final Map<Integer, Statistics> oneMinuteStatistics = new ConcurrentHashMap<>(DURATION_STATS);

	/**
	 * Re-calculates statistics for last 60 seconds based on new received
	 * transaction data. Transaction data summary (sum, max, min, count) is kept
	 * inside map, for each second of last minute new summary entry is created
	 * or existing one is updated with latest statistics for that second, if
	 * previously inserted entry is outdated then it will be overwritten by new
	 * one(s) (if no new transaction entry is received for that second then
	 * it'll be simply ignored during statistics retrieval).
	 *
	 * Application holds constant memory storage (at max 60 entries) about
	 * statistics of last minute, which means memory complexity is O(1)
	 *
	 * @param transaction
	 *            new transaction data
	 */
	@Override
	public void computeStatistics(Transaction transaction) {
		logger.info("Computing statistics based on new received transaction => {}", transaction);

		if (null == transaction) {
			throw new RequestValidationException(APIErrors.VALIDATION_EMPTY_REQUEST_BODY);
		}

		else if (!Optional.ofNullable(transaction.getAmount()).isPresent()) {
			throw new RequestValidationException(APIErrors.VALIDATION_MISSING_AMOUNT);
		}

		else if ((System.currentTimeMillis() - transaction.getTimestamp()) / 1000 < DURATION_STATS) {
			int second = LocalDateTime
					.ofInstant(Instant.ofEpochMilli(transaction.getTimestamp()), ZoneId.systemDefault()).getSecond();
			oneMinuteStatistics.compute(second, (k, v) -> {
				if (v == null || (System.currentTimeMillis() - v.getTimestamp()) / 1000 >= DURATION_STATS) {
					v = new Statistics();
					v.setTimestamp(transaction.getTimestamp());
					v.setAmount(transaction.getAmount());
					return v;
				}
				v.setAmount(v.getAmount() + transaction.getAmount());
				return v;
			});
		}
	}

	/**
	 * Calculates and returns combined statistics summary based on statistics
	 * map (statisticsForLastMin). During calculation outdated statistics are
	 * ignored.
	 *
	 * Calculation is made in constant time by only combining already calculated
	 * statistics, which means method runs with O(1) complexity
	 *
	 * @return computed statistics summary
	 */
	@Override
	public ComputedStatistics getStatistics() {
		DoubleSummaryStatistics summary = oneMinuteStatistics.values().parallelStream()
				.filter(s -> (System.currentTimeMillis() - s.getTimestamp()) / 1000 < DURATION_STATS)
				.collect(Collectors.summarizingDouble(Statistics::getAmount));

		ComputedStatistics computedStatistics = new ComputedStatistics(summary);

		logger.info("Statistics summary for last minute => {}", summary);
		return computedStatistics;
	}
}
