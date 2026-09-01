# Changes Made to the Project

This document describes the changes and improvements I made to the project.

### 1. Project Setup
-Forked and cloned the original project.
-Installed and configured Java 17.
-Configured JAVA_HOME.
-Opened the project in VS Code.

### 2. Project Verification
-Ran the Maven test command and verified the sample test.
-Started the Spring Boot application successfully.
-Tested the existing GET /api/sample endpoint.

### 3. Transaction Entity
-Created the Transaction entity with transaction ID, customer ID, amount, currency, transaction type, and status.
-Added validation for the transaction fields.
-Set transactionId as the primary key.

### 4. Transaction Repository
-Created TransactionRepository.
-Extended JpaRepository for database operations.
-Added customer-based transaction lookup.

### 5. Create Transaction API
-Added the service and controller layers.
-Implemented POST /api/transactions.
-Connected the API with the repository and H2 database.
-Tested transaction creation successfully.

### 6. Get Transaction API
-Implemented GET /api/transactions/{transactionId}.
-Returns the transaction when found.
-Returns 404 Not Found when it does not exist.

### 7. Update Transaction Status API
-Implemented PUT /api/transactions/{transactionId}/status.
-Allowed status changes from PENDING to COMPLETED or FAILED.
-Prevented changes after the transaction is completed or failed.

### 8. Get Customer Transactions API
-Implemented GET /api/transactions/customer/{customerId}.
-Returns all transactions belonging to the specified customer.

### 9. Error Handling
-Added validation using Jakarta Bean Validation.
-Added a global exception handler using @RestControllerAdvice.
-Validation and invalid transaction operations return 400 Bad Request.
-Missing transactions return 404 Not Found.

### 10. Automated Tests
-Added integration tests for creating, retrieving, and updating transactions.
-Added tests for customer transaction lookup.
-Added tests for invalid data, missing transactions, and invalid status changes.
-Verified the APIs using Spring Boot test infrastructure.

### 11. Final Test Verification
-Ran the complete Maven test suite.
-Verified the main transaction operations and error cases.
-All 9 tests passed with 0 failures and 0 errors.
-AI Usage Disclosure

### Tools used: ChatGPT.
I used ChatGPT as a coding assistant to understand the project requirements, Spring Boot code, validation, exception handling, APIs, and test cases.

## What I changed, corrected or rejected, and why
I made the validation stricter by adding checks for ID length, amount, currency, transaction type, and transaction status. I also set PENDING as the default status based on the project requirements.

## Anything the AI got wrong that I had to fix
Some initial test cases expected incorrect HTTP status codes. I checked the actual application behavior, corrected the tests, and ran them again.

## How I checked the final result actually works
I ran the application and tested the REST APIs using my integration tests. I also ran the complete Maven test suite and confirmed that all 9 tests passed with 0 failures and 0 errors.
