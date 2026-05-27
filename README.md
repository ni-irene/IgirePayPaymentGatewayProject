# IgirePay Payment Gateway Project

## Project Description
IgirePay Payment Gateway Project is a Java Maven application that simulates a payment gateway system. It supports customer management, account management, transaction handling, loan management, airtime purchase, reporting, and a JavaFX graphical user interface.

---

## Technologies Used

| Technology     | Purpose                        |
|----------------|-------------------------------|
| Java 17        | Core programming language      |
| Maven          | Build and dependency management|
| PostgreSQL     | Relational database            |
| JDBC           | Database connectivity          |
| JavaFX         | Graphical User Interface       |
| IntelliJ IDEA  | IDE                            |
| Git & GitHub   | Version control & collaboration|

---

## Project Structure

```
src/main/java/org/igirepay/igirepaypaymentgatewayproject/
│
├── LAB1/
│   ├── model/
│   │   ├── Account.java               # Base account class
│   │   ├── SavingsAccount.java        # Extends Account, adds withdrawal fee
│   │   ├── WalletAccount.java         # Extends Account, standard withdrawal
│   │   ├── Customer.java              # Customer model with PIN, role, lock
│   │   ├── Transaction.java           # Transaction model
│   │   └── Loan.java                  # Loan model with due date and status
│   └── service/
│       └── TransactionService.java    # In-memory duplicate detection
│
├── LAB2/
│   ├── dao/
│   │   ├── AccountDAO.java
│   │   ├── CustomerDAO.java
│   │   ├── TransactionDAO.java
│   │   ├── LoanDAO.java
│   │   └── ProcessedRequestDAO.java
│   ├── daoImpl/
│   │   ├── AccountDAOImpl.java        # JDBC implementation for accounts
│   │   ├── CustomerDAOImpl.java       # JDBC implementation for customers
│   │   ├── TransactionDAOImpl.java    # JDBC implementation for transactions
│   │   ├── LoanDAOImpl.java           # JDBC implementation for loans
│   │   └── ProcessedRequestDAOImpl.java # Duplicate reference tracking
│   └── util/
│       └── DBConnection.java          # PostgreSQL connection utility
│
├── LAB3/
│   ├── exception/
│   │   ├── InvalidAmountException.java
│   │   ├── InsufficientBalanceException.java
│   │   ├── DuplicateTransactionException.java
│   │   └── LoanException.java
│   ├── report/
│   │   └── ReportService.java         # CSV export, daily summary, statements
│   └── service/
│       └── PaymentService.java        # Core payment business logic
│
├── HelloApplication.java              # JavaFX entry point
├── HelloController.java               # JavaFX controller (all UI logic)
└── Main.java                          # Console-based menu entry point
```

---

## Features

- **Customer Management** — Register, update, delete customers. Admin can lock/unlock accounts
- **Account Management** — Create Wallet and Savings accounts per customer
- **Deposit / Withdraw / Transfer** — With duplicate reference detection and balance validation
- **Push/Pull Savings** — Move funds between Wallet and Savings accounts
- **Buy Airtime** — Deduct from wallet and log airtime transaction
- **Loan Management** — Take loans (max 50% of savings balance), repay with overpayment protection
- **Reports** — Export transactions to CSV, view daily summary, view account statement
- **Role-based UI** — Admin dashboard and Customer dashboard with login/logout
- **Account Security** — PIN authentication, account locks after 3 failed attempts

---

## Git/GitHub Collaboration Workflow

This project follows Git and GitHub collaboration workflows as required in Exercise 3.5.

---

### 1. Feature Branches

A separate branch was created for each lab:

```bash
git checkout -b feature/lab1-models
git checkout -b feature/lab2-dao
git checkout -b feature/lab3-services
```

| Branch                  | Contents                                              |
|-------------------------|-------------------------------------------------------|
| `feature/lab1-models`   | Domain models and TransactionService                  |
| `feature/lab2-dao`      | DAO interfaces, implementations, DBConnection         |
| `feature/lab3-services` | Exceptions, PaymentService, ReportService, JavaFX UI  |

---

### 2. Pull Requests

Each feature branch was pushed to GitHub and merged into `main` via a Pull Request:

```bash
git push origin feature/lab1-models
git push origin feature/lab2-dao
git push origin feature/lab3-services
```

Then on GitHub: **Compare & pull request → Add description → Merge pull request**

---

### 3. Merge Conflict Resolution

Merge conflicts were simulated and resolved during branch merging by editing conflicting files, removing conflict markers, and committing the resolved version.

---

### 4. Clean Commit History

```
Initial empty commit
chore: Add project setup (pom.xml, Maven wrapper, module-info, schema)
LAB1: Add core domain models - Account, SavingsAccount, WalletAccount, Customer, Transaction, Loan, TransactionService
Merge feature/lab1-models: Core domain models and TransactionService
LAB2: Add DAO interfaces, implementations and DBConnection for Customer, Account, Transaction, Loan, ProcessedRequest
Merge feature/lab2-dao: DAO layer with JDBC PostgreSQL integration
LAB3: Add custom exceptions, PaymentService, ReportService, JavaFX UI (HelloController, FXML, Main)
Merge feature/lab3-services: Exceptions, PaymentService, ReportService and JavaFX UI
Update README: fix project structure, branch names and commit history
```

---

## Git Commands Reference

```bash
# Create a feature branch
git checkout -b feature/lab1-models

# Stage changes
git add .

# Commit with a meaningful message
git commit -m "LAB1: Add core domain models"

# Push branch to GitHub
git push origin feature/lab1-models

# Merge branch into main locally
git checkout main
git merge feature/lab1-models

# View commit history
git log --oneline --graph --all
```

---

## Author

**Igiraneza Irene**

---

## Repository

[https://github.com/ni-irene/IgirePayPaymentGatewayProject](https://github.com/ni-irene/IgirePayPaymentGatewayProject)
