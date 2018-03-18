package com.transactions.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler class which returns meaningful error responses in
 * case if exception is thrown in internal methods
 *
 * @author shivamoberoi
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(value = RequestValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RequestValidationException handleException(RequestValidationException exception) {
		logger.error(exception.getMessage(), exception);
		return new RequestValidationException(exception.getErrorMessage());
	}
}
