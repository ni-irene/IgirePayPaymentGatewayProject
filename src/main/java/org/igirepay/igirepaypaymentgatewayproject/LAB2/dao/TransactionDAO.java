package org.igirepay.igirepaypaymentgatewayproject.LAB2.dao;

public interface TransactionDAO {

        void saveTransaction(
                int accountId,
                String referenceId,
                String transactionType,
                double amount
        );

        void viewTransactions();

        void viewTransactionsByAccount(int accountId);

}
