package com.transactions;

import static com.jayway.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.transactions.model.Transaction;

/**
 * @author ankitgoyal
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionControllerTests {

	@LocalServerPort
	private int port;

	private Transaction transaction;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void TransactionBeforeOneMinute() {
		transaction = new Transaction(10D, System.currentTimeMillis());
		Response resp = given().contentType("application/json").body(transaction).when().post("/transactions");
		Assertions.assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
	}

	@Test
	public void TransactionAfterOneMinute() {
		transaction = new Transaction(10D, (System.currentTimeMillis() - 600000));
		Response resp = given().contentType("application/json").body(transaction).when().post("/transactions");
		Assertions.assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
	}
}
