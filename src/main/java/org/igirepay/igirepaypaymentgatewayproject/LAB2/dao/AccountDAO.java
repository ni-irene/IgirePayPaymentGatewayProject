package org.igirepay.igirepaypaymentgatewayproject.LAB2.dao;

public interface AccountDAO {


        void createAccount(
                int customerId,
                String accountType,
                double balance
        );

        boolean accountTypeExists(int customerId, String accountType);

        void viewAccounts();

        void viewAccountsByCustomer(int customerId);

        void deposit(
                int accountId,
                double amount
        );

        void withdraw(
                int accountId,
                double amount
        );

        double checkBalance(int accountId);

        void deleteAccount(int accountId);

        void transfer(int fromAccountId, int toAccountId, double amount);

        int getAccountIdByCustomerAndType(int customerId, String accountType);

}
