package com.transactions.common;

/**
 * API Error Codes
 *
 * @author ankitgoyal
 */
public enum APIErrors {
	VALIDATION("Parameters Validation error"), VALIDATION_EMPTY_REQUEST_BODY(
			"Empty request body"), VALIDATION_MISSING_TIMESTAMP(
					"Missing timestamp field"), VALIDATION_MISSING_AMOUNT("Missing Amount field"),

	UNEXPECTED_ERROR("Internal API Error");

	private String message;

	APIErrors(String message) {
		this.message = message;
	}

	public String message() {
		return this.message;
	}
}
