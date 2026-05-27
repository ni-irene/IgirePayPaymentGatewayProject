package org.igirepay.igirepaypaymentgatewayproject.LAB1.model;

import java.time.LocalDateTime;

public class Loan {

    private int id;
    private int customerId;
    private int savingsAccountId;
    private double amount;
    private double amountPaid;
    private double loanLimit;
    private String status;           // ACTIVE, PAID, OVERDUE
    private LocalDateTime takenAt;
    private LocalDateTime dueDate;

    public Loan() {}

    public Loan(int customerId, int savingsAccountId, double amount, double loanLimit) {
        this.customerId = customerId;
        this.savingsAccountId = savingsAccountId;
        this.amount = amount;
        this.loanLimit = loanLimit;
        this.amountPaid = 0;
        this.status = "ACTIVE";
        this.takenAt = LocalDateTime.now();
        this.dueDate = takenAt.plusDays(30);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getSavingsAccountId() { return savingsAccountId; }
    public void setSavingsAccountId(int savingsAccountId) { this.savingsAccountId = savingsAccountId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public double getLoanLimit() { return loanLimit; }
    public void setLoanLimit(double loanLimit) { this.loanLimit = loanLimit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTakenAt() { return takenAt; }
    public void setTakenAt(LocalDateTime takenAt) { this.takenAt = takenAt; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public double getRemainingBalance() { return amount - amountPaid; }

    @Override
    public String toString() {
        return "Loan{id=" + id + ", amount=" + amount + ", paid=" + amountPaid +
               ", remaining=" + getRemainingBalance() + ", status=" + status +
               ", dueDate=" + dueDate + "}";
    }
}
