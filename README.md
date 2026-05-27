# IgirePay Payment Gateway Project

## Project Description
IgirePay Payment Gateway Project is a Java Maven application designed to simulate a payment gateway system with customer management, transaction handling, reporting, and service modules.

---

# Technologies Used

- Java
- Maven
- PostgreSQL
- IntelliJ IDEA
- Git & GitHub

---

# Project Structure

```text
src/main/java/org/igirepay/igirepaypaymentgatewayproject/

├── LAB1
│   ├── models
│   └── service
│
├── LAB2
│   ├── dao
│   ├── daoImpl
│   └── util
│
├── LAB3
│   ├── exceptions
│   ├── reports
│   └── paymentservice
```

---

# Git/GitHub Collaboration Workflow

This project demonstrates Git and GitHub collaboration workflows as required in Exercise 3.5.

## Implemented Requirements

### 1. Feature Branches
Feature branches were created to develop functionalities independently.

Examples:

- feature/customer-management
- feature/transaction-module
- feature/report-service

---

### 2. Pull Requests
Feature branches are merged into the main branch using Pull Requests on GitHub.

---

### 3. Merge Conflict Resolution
Merge conflicts were simulated and resolved successfully during branch merging.

---

### 4. Clean Commit History
Meaningful commit messages were maintained throughout development.

Example commit messages:

```bash
Initial project setup
Add customer registration functionality
Implement transaction DAO
Create report generation module
Resolve merge conflict in Main.java
```

---

# Git Commands Used

## Create Branch

```bash
git checkout -b feature/customer-management
```

## Add Changes

```bash
git add .
```

## Commit Changes

```bash
git commit -m "Add customer registration functionality"
```

## Push Branch

```bash
git push origin feature/customer-management
```

## Merge Branch

```bash
git checkout main
git merge feature/customer-management
```

---

# Author

Igiraneza Irene

---

# Repository Link

https://github.com/ni-irene/IgirePayPaymentGatewayProject
