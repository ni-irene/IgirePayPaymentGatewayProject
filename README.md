# IgirePay Payment Gateway Project

## Project Description
IgirePay Payment Gateway Project is a Java Maven application designed to simulate a payment gateway system with customer management, transaction handling, loan management, reporting, and a JavaFX user interface.

---

## Technologies Used

- Java 17
- Maven
- PostgreSQL
- JavaFX
- IntelliJ IDEA
- Git & GitHub

---

## Project Structure

```text
src/main/java/org/igirepay/igirepaypaymentgatewayproject/

├── LAB1
│   ├── model
│   │   ├── Account.java
│   │   ├── SavingsAccount.java
│   │   ├── WalletAccount.java
│   │   ├── Customer.java
│   │   ├── Transaction.java
│   │   └── Loan.java
│   └── service
│       └── TransactionService.java
│
├── LAB2
│   ├── dao
│   │   ├── AccountDAO.java
│   │   ├── CustomerDAO.java
│   │   ├── TransactionDAO.java
│   │   ├── LoanDAO.java
│   │   └── ProcessedRequestDAO.java
│   ├── daoImpl
│   │   ├── AccountDAOImpl.java
│   │   ├── CustomerDAOImpl.java
│   │   ├── TransactionDAOImpl.java
│   │   ├── LoanDAOImpl.java
│   │   └── ProcessedRequestDAOImpl.java
│   └── util
│       └── DBConnection.java
│
├── LAB3
│   ├── exception
│   │   ├── InvalidAmountException.java
│   │   ├── InsufficientBalanceException.java
│   │   ├── DuplicateTransactionException.java
│   │   └── LoanException.java
│   ├── report
│   │   └── ReportService.java
│   └── service
│       └── PaymentService.java
│
├── HelloApplication.java
├── HelloController.java
└── Main.java
```

---

## Git/GitHub Collaboration Workflow

This project demonstrates Git and GitHub collaboration workflows as required in Exercise 3.5.

---

### 1. Feature Branches
Feature branches were created to develop each lab independently.

- `feature/lab1-models` — Core domain models and TransactionService
- `feature/lab2-dao` — DAO interfaces, implementations and DBConnection
- `feature/lab3-services` — Exceptions, PaymentService, ReportService and JavaFX UI

---

### 2. Pull Requests
Each feature branch was pushed to GitHub and merged into `main` using Pull Requests.

---

### 3. Merge Conflict Resolution
Merge conflicts were simulated and resolved successfully during branch merging.

---

### 4. Clean Commit History

```bash
Initial empty commit
chore: Add project setup (pom.xml, Maven wrapper, module-info, schema)
LAB1: Add core domain models - Account, SavingsAccount, WalletAccount, Customer, Transaction, Loan, TransactionService
Merge feature/lab1-models: Core domain models and TransactionService
LAB2: Add DAO interfaces, implementations and DBConnection for Customer, Account, Transaction, Loan, ProcessedRequest
Merge feature/lab2-dao: DAO layer with JDBC PostgreSQL integration
LAB3: Add custom exceptions, PaymentService, ReportService, JavaFX UI (HelloController, FXML, Main)
Merge feature/lab3-services: Exceptions, PaymentService, ReportService and JavaFX UI
```

---

## Git Commands Used

### Create Branch
```bash
git checkout -b feature/lab1-models
```

### Add Changes
```bash
git add .
```

### Commit Changes
```bash
git commit -m "LAB1: Add core domain models"
```

### Push Branch
```bash
git push origin feature/lab1-models
```

### Merge Branch
```bash
git checkout main
git merge feature/lab1-models
```

---

## Author

Igiraneza Irene

---

## Repository Link

https://github.com/ni-irene/IgirePayPaymentGatewayProject
