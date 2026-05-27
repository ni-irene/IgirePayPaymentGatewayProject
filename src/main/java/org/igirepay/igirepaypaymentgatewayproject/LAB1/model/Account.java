package org.igirepay.igirepaypaymentgatewayproject.LAB1.model;

/**
 * Base class representing a bank account.
 * Extended by SavingsAccount and WalletAccount.
 */
public class Account {

        protected int id;
        protected double balance;

        public Account() {
        }

        public Account(int id, double balance) {
            this.id = id;
            this.balance = balance;
        }

        public void deposit(double amount) {
            balance += amount;
        }

        public void withdraw(double amount) {
            balance -= amount;
        }

        public int getId() {
            return id;
        }

        public double getBalance() {
            return balance;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "id=" + id +
                    ", balance=" + balance +
                    '}';
        }

}
