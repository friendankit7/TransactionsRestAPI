package com.transactions.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transactions.common.APIErrors;
import com.transactions.common.ApplicationConstants;
import com.transactions.exception.RequestValidationException;
import com.transactions.model.Transaction;
import com.transactions.service.TransactionService;

/**
 * Controller for Incoming Transactions.
 *
 * @author ankitgoyal
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {


	@Autowired
	private TransactionService transactionService;

	@PostMapping
	public ResponseEntity<Void> addTransaction(@RequestBody Transaction transaction) {
		if (!Optional.ofNullable(transaction.getTimestamp()).isPresent()) {
			throw new RequestValidationException(APIErrors.VALIDATION_MISSING_TIMESTAMP);
		} else if ((System.currentTimeMillis() - transaction.getTimestamp()) / 1000 > ApplicationConstants.TIME_LIMIT) {
			// if transaction did not happened within the last minute
			return new ResponseEntity<>(NO_CONTENT);
		} else {
			transactionService.addTransaction(transaction);
			return new ResponseEntity<>(CREATED);
		}
	}
}
