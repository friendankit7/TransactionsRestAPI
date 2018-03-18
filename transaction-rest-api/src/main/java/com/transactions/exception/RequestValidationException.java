package com.transactions.exception;

import com.transactions.common.APIErrors;

/**
 * Unchecked exception is thrown if request body is not valid or it has some
 * invalid fields
 *
 * @author ankitgoyal
 */
public class RequestValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorMessage;

	public RequestValidationException() {
		super(APIErrors.VALIDATION.message());
		this.errorMessage = APIErrors.VALIDATION.message();
	}

	public RequestValidationException(APIErrors APIErrors) {
		super(APIErrors.message());

		this.errorMessage = APIErrors.message();
	}

	public RequestValidationException(String errorMessage) {
		super(errorMessage);

		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
