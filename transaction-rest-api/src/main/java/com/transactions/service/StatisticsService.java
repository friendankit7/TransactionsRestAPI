package com.transactions.service;

import com.transactions.model.ComputedStatistics;
import com.transactions.model.Transaction;

/**
 * @author ankitgoyal
 */
public interface StatisticsService {

    void computeStatistics(Transaction transaction);

    ComputedStatistics getStatistics();

}
