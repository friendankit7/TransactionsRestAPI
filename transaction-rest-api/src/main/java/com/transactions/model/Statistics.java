package com.transactions.model;

/**
 * Data object to hold statistics
 *
 * @author ankitgoyal
 */
public class Statistics {
	private long timestamp;
	private double amount;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
