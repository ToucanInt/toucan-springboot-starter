package com.example.transactionstarter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    // Create a new transaction using POST /api/transactions

    @Test
    void shouldCreateTransaction() {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN-20260901-0001");
        transaction.setCustomerId("CUSTOMER001");
        transaction.setAmount(new BigDecimal("1000.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType("CARD");
        transaction.setTransactionStatus("PENDING");

        ResponseEntity<Transaction> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/transactions",
                        transaction,
                        Transaction.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(
                "TXN-20260901-0001",
                response.getBody().getTransactionId());
    }


    // Get a transaction by its transaction ID

    @Test
    void shouldGetTransaction() {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN-20260901-0002");
        transaction.setCustomerId("CUSTOMER001");
        transaction.setAmount(new BigDecimal("500.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType("ONLINE");
        transaction.setTransactionStatus("PENDING");

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/transactions",
                transaction,
                Transaction.class);

        ResponseEntity<Transaction> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port +
                                "/api/transactions/TXN-20260901-0002",
                        Transaction.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(
                "TXN-20260901-0002",
                response.getBody().getTransactionId());
    }


    // Update a PENDING transaction to COMPLETED

    @Test
    void shouldUpdateTransactionStatus() {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN-20260901-0003");
        transaction.setCustomerId("CUSTOMER001");
        transaction.setAmount(new BigDecimal("750.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType("BANK_TRANSFER");
        transaction.setTransactionStatus("PENDING");

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/transactions",
                transaction,
                Transaction.class);

        ResponseEntity<Transaction> response =
                restTemplate.exchange(
                        "http://localhost:" + port +
                                "/api/transactions/TXN-20260901-0003/status?status=COMPLETED",
                        HttpMethod.PUT,
                        null,
                        Transaction.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(
                "COMPLETED",
                response.getBody().getTransactionStatus());
    }


    // Get all transactions belonging to a customer

    @Test
    void shouldGetTransactionsByCustomer() {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN-20260901-0004");
        transaction.setCustomerId("CUSTOMER002");
        transaction.setAmount(new BigDecimal("1200.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType("ONLINE");
        transaction.setTransactionStatus("PENDING");

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/transactions",
                transaction,
                Transaction.class);

        ResponseEntity<Transaction[]> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port +
                                "/api/transactions/customer/CUSTOMER002",
                        Transaction[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 1);
    }


    // Returns 404 when the transaction does not exist

    @Test
    void shouldReturnNotFoundForInvalidTransactionId() {

        ResponseEntity<Transaction> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port +
                                "/api/transactions/INVALID001",
                        Transaction.class);

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode());
    }


    // Reject invalid transaction data

    @Test
    void shouldRejectInvalidTransaction() {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN-20260901-0005");
        transaction.setCustomerId("");
        transaction.setAmount(new BigDecimal("-100.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType("BANK_TRANSFER");
        transaction.setTransactionStatus("PENDING");

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/transactions",
                        transaction,
                        String.class);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());
    }


    // Prevent changing a transaction after it is COMPLETED

    @Test
    void shouldNotAllowStatusChangeFromCompleted() {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN-20260901-0006");
        transaction.setCustomerId("CUSTOMER003");
        transaction.setAmount(new BigDecimal("500.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType("UPI");
        transaction.setTransactionStatus("PENDING");

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/transactions",
                transaction,
                Transaction.class);

        // Change PENDING to COMPLETED

        restTemplate.exchange(
                "http://localhost:" + port +
                        "/api/transactions/TXN-20260901-0006/status?status=COMPLETED",
                HttpMethod.PUT,
                null,
                Transaction.class);

        // Try to change COMPLETED to FAILED

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "http://localhost:" + port +
                                "/api/transactions/TXN-20260901-0006/status?status=FAILED",
                        HttpMethod.PUT,
                        null,
                        String.class);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());
    }


    // Prevent status change after transaction has FAILED

    @Test
    void shouldNotAllowStatusChangeFromFailed() {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN-20260901-0008");
        transaction.setCustomerId("CUSTOMER005");
        transaction.setAmount(new BigDecimal("800.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType("UPI");
        transaction.setTransactionStatus("PENDING");

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/transactions",
                transaction,
                Transaction.class);

        // Change PENDING to FAILED

        restTemplate.exchange(
                "http://localhost:" + port +
                        "/api/transactions/TXN-20260901-0008/status?status=FAILED",
                HttpMethod.PUT,
                null,
                Transaction.class);

        // Try to change FAILED to COMPLETED

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "http://localhost:" + port +
                                "/api/transactions/TXN-20260901-0008/status?status=COMPLETED",
                        HttpMethod.PUT,
                        null,
                        String.class);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());
    }


    // Multiple transactions for the same customer

    @Test
    void shouldHandleMultipleTransactionsForSameCustomer() {

        Transaction transaction1 = new Transaction();

        transaction1.setTransactionId("TXN-20260901-0009");
        transaction1.setCustomerId("CUSTOMER006");
        transaction1.setAmount(new BigDecimal("500.00"));
        transaction1.setCurrency("INR");
        transaction1.setTransactionType("UPI");
        transaction1.setTransactionStatus("PENDING");


        Transaction transaction2 = new Transaction();

        transaction2.setTransactionId("TXN-20260901-0010");
        transaction2.setCustomerId("CUSTOMER006");
        transaction2.setAmount(new BigDecimal("1500.00"));
        transaction2.setCurrency("INR");
        transaction2.setTransactionType("CARD");
        transaction2.setTransactionStatus("PENDING");


        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/transactions",
                transaction1,
                Transaction.class);

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/transactions",
                transaction2,
                Transaction.class);


        ResponseEntity<Transaction[]> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port +
                                "/api/transactions/customer/CUSTOMER006",
                        Transaction[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(2, response.getBody().length);
    }
}