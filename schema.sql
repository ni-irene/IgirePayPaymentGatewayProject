-- ─────────────────────────────────────────────────────────────────────────────
-- IgirePay Payment Gateway — PostgreSQL Database Schema
-- Author: Igiraneza Irene
-- Description: Defines all tables for the IgirePay payment gateway system
--              including customers, accounts, transactions, loans and
--              duplicate request tracking.
-- Usage: psql -U postgres -d igirepay -f schema.sql
-- ─────────────────────────────────────────────────────────────────────────────

-- ─── Customers ───────────────────────────────────────────────────────────────
-- Stores all registered customers and admin users.
-- role: 'customer' for regular users, 'admin' for administrators
-- locked: set to TRUE after 3 failed PIN attempts
-- failed_attempts: tracks consecutive failed login attempts
CREATE TABLE customers (
    id               SERIAL PRIMARY KEY,
    full_name        VARCHAR(100)        NOT NULL,
    email            VARCHAR(100)        UNIQUE,
    phone_number     VARCHAR(20),
    pin              VARCHAR(10)         NOT NULL,
    role             VARCHAR(20)         NOT NULL DEFAULT 'customer',
    locked           BOOLEAN             NOT NULL DEFAULT FALSE,
    failed_attempts  INT                 NOT NULL DEFAULT 0,
    created_at       TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- ─── Accounts ────────────────────────────────────────────────────────────────
-- Each customer can have two account types: Wallet and Savings.
-- Wallet: used for daily transactions (deposit, withdraw, transfer, airtime)
-- Savings: used for saving money and as collateral for loans
-- customer_id: foreign key linking account to its owner (cascades on delete)
CREATE TABLE accounts (
    id               SERIAL PRIMARY KEY,
    customer_id      INT                 NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    account_type     VARCHAR(20)         NOT NULL CHECK (account_type IN ('Wallet','Savings')),
    balance          NUMERIC(15,2)       NOT NULL DEFAULT 0.00,
    created_at       TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- ─── Transactions ─────────────────────────────────────────────────────────────
-- Records every financial operation performed on an account.
-- transaction_type examples: Deposit, Withdraw, Transfer-Out, Transfer-In,
--                            Push-To-Savings, Pull-From-Savings, Airtime,
--                            Loan-Disbursed, Loan-Repayment
-- reference_id: unique identifier per transaction for idempotency checks
CREATE TABLE transactions (
    id               SERIAL PRIMARY KEY,
    account_id       INT                 NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    reference_id     VARCHAR(100)        NOT NULL,
    transaction_type VARCHAR(50)         NOT NULL,
    amount           NUMERIC(15,2)       NOT NULL,
    created_at       TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- ─── Processed Requests (Idempotency) ────────────────────────────────────────
-- Prevents duplicate transactions by storing all processed reference IDs.
-- Before processing any transaction, the system checks this table.
-- If the reference_id already exists, the transaction is rejected.
CREATE TABLE processed_requests (
    id               SERIAL PRIMARY KEY,
    reference_id     VARCHAR(100)        NOT NULL UNIQUE,
    processed_at     TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- ─── Loans ───────────────────────────────────────────────────────────────────
-- Tracks loans taken by customers against their savings account balance.
-- loan_limit: maximum loan allowed (50% of savings balance at time of request)
-- amount_paid: tracks repayments made so far
-- status: ACTIVE (ongoing), PAID (fully repaid), OVERDUE (past due date)
-- due_date: automatically set to 30 days from the date the loan was taken
CREATE TABLE loans (
    id                  SERIAL PRIMARY KEY,
    customer_id         INT             NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    savings_account_id  INT             NOT NULL REFERENCES accounts(id),
    amount              NUMERIC(15,2)   NOT NULL,
    amount_paid         NUMERIC(15,2)   NOT NULL DEFAULT 0.00,
    loan_limit          NUMERIC(15,2)   NOT NULL,
    status              VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE'
                                        CHECK (status IN ('ACTIVE','PAID','OVERDUE')),
    taken_at            TIMESTAMP       NOT NULL DEFAULT NOW(),
    due_date            TIMESTAMP       NOT NULL DEFAULT NOW() + INTERVAL '30 days'
);

-- ─── Default Admin Account ───────────────────────────────────────────────────
-- Creates a default admin user on first run.
-- Login: Customer ID (auto-assigned), PIN: 00000
-- Use this account to register customers and manage the system.
INSERT INTO customers (full_name, email, phone_number, pin, role)
VALUES ('Admin', 'admin@igirepay.com', '+250788000000', '00000', 'admin')
ON CONFLICT DO NOTHING;
