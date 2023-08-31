# hombanking-java
Homebanking Demo project for Spring Boot, (Spring Data JPA, H2 database, Spring Web y Rest Repositories.)
# Objectives of each task
#  Task1
- Using Spring Boot and Java to create a RESTful web API

- Create a database of customers registered in the system

- Create a table in the customer database, each customer has a first name, last name, and email.

- Being able to see the list of clients from the web service [http://localhost:8080/clients](http://localhost:8080/clients)

# Task2
The objectives of this task2 were:
* Create both back-end and front-end code of the elements to create are:
  - Code for the Account entity and the test data
  - Code to create an application controller that returns a JSON with the information of the Client and its associated accounts.
  - HTML, CSS and Javascript code that displays the information in a user-readable way.

# Task3
The objectives of this task3 were:
- Create the Transaction entity with the fields indicated in the model and create its repository.
- Java code to create an Account controller that returns all accounts and a particular account.
- A web page that shows the information of the account and the list of transactions of the same.

# Task4
The objectives of this task4 were:
- Create the Loan entity and the repository
- Create the test data
- Test automatic REST services
- Create the ClientLoan entity and the repository
- Modify the account view to show loans

# Task5
The objectives of this task5 were:
  ## Create the Card entity and the repository
- Create the test data:
  - In the CommandLineRunner you must create:
    - A GOLD debit card for the client Melba, the start date of validity is the current date and the expiration date 5 years from the current date, cardholder will have the name and surname of the concatenated customer, the other fields can be completed at your choice, remember that the cvv has only 3 digits.
    - A Titanium credit card for the client Melba with the same data except number and cvv.
    - Create a silver credit card for the second customer.
- Try the automatic REST services (go to the routes /rest/cards and /rest/clients/1/cards and verify that the cards have been created)
- Add the information of the cards to the JSON of clients (Modify the ClientDTO to show the information of the client's cards)
- Verify that the data is displayed correctly on the front

# Task6
  ## Implement client registration and login
The objectives of this task6 were:
- Modify the customer entity to include a password field
- Add code on the server to handle login and logout
- Add a method in the client controller that allows creating new clients if the information sent is correct
- Create a web page that has a form to register and to log in with the necessary Javascript code to send the information to the server.
- Add a method in the client controller that allows obtaining the data of an authenticated client.
- Verify that the data is displayed correctly on the front

# Task7 
 ## Add creating accounts and cards
- Create a new resource to create accounts
- Implement the create accounts button
- Create account when registering a client
- Create card controller

# Task8
##  Add making transactions
- Understand the concept of transaction and comply with the ACID model (Atomicity, Consistency, Isolation and Durability)
- Create a resource(controller) to perform transactions, keep in mind:
  - You must receive the amount, description, origin account number and destination account number as request parameters
  - Check that the parameters are not empty
  - Verify that the account numbers are not the same
  - Verify that the source account exists
  - Verify that the source account belongs to the authenticated client
  - Verify that the destination account exists
  - Verify that the source account has the amount available.
  - Two transactions must be created, one with the transaction type "DEBIT" associated with the source account and the other with the transaction type "CREDIT" associated with the destination account.
  - The amount indicated in the request will be subtracted from the origin account and the same amount will be added to the destination account.

# Task9
## Add applying for loans
- Create a resource to apply for loans
  - You must receive a credit application object with the loan data
  - Verify that the data is correct, that is, they are not empty, that the amount is not 0 or that the fees are not 0.
  - Verify that the loan exists
  - Verify that the amount requested does not exceed the maximum amount of the loan
  - Verify that the number of installments is among those available for the loan
  - Verify that the target account exists
  - Verify that the destination account belongs to the authenticated client
  - A loan application must be created with the requested amount adding 20% of it
  - A "CREDIT" transaction must be created associated with the destination account (the amount must be positive) with the description concatenating the name of the loan and the phrase "loan approved"
  - The destination account must be updated adding the requested amount.


