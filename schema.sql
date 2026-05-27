-- IgirePay Payment Gateway — PostgreSQL Schema
-- Run once: psql -U postgres -d igirepay -f schema.sql

-- ─── Customers ───────────────────────────────────────────────────────────────
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
CREATE TABLE accounts (
    id               SERIAL PRIMARY KEY,
    customer_id      INT                 NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    account_type     VARCHAR(20)         NOT NULL CHECK (account_type IN ('Wallet','Savings')),
    balance          NUMERIC(15,2)       NOT NULL DEFAULT 0.00,
    created_at       TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- ─── Transactions ─────────────────────────────────────────────────────────────
CREATE TABLE transactions (
    id               SERIAL PRIMARY KEY,
    account_id       INT                 NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    reference_id     VARCHAR(100)        NOT NULL,
    transaction_type VARCHAR(50)         NOT NULL,
    amount           NUMERIC(15,2)       NOT NULL,
    created_at       TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- ─── Processed Requests (idempotency) ────────────────────────────────────────
CREATE TABLE processed_requests (
    id               SERIAL PRIMARY KEY,
    reference_id     VARCHAR(100)        NOT NULL UNIQUE,
    processed_at     TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- ─── Loans ───────────────────────────────────────────────────────────────────
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

-- ─── Default admin account (PIN: 00000) ──────────────────────────────────────
INSERT INTO customers (full_name, email, phone_number, pin, role)
VALUES ('Admin', 'admin@igirepay.com', '+250788000000', '00000', 'admin')
ON CONFLICT DO NOTHING;
