package org.igirepay.igirepaypaymentgatewayproject.LAB1.service;

import org.igirepay.igirepaypaymentgatewayproject.LAB1.model.Transaction;

import java.time.LocalDateTime;
import java.util.*;

public class TransactionService {

        // Store transaction history
        private List<Transaction> transactions =
                new ArrayList<>();

        // Store processed reference IDs
        private Set<String> processedReferenceIds =
                new HashSet<>();

        // Store failed transaction logs
        private Map<String, String> failedTransactions =
                new HashMap<>();


        public void processTransaction(
                Transaction transaction
        ) {

            // Duplicate detection
            if(processedReferenceIds.contains(
                    transaction.getReferenceId()
            )) {

                failedTransactions.put(
                        transaction.getReferenceId(),
                        "Duplicate transaction detected"
                );

                System.out.println(
                        "Duplicate transaction rejected!"
                );

                return;
            }

            // Add reference ID
            processedReferenceIds.add(
                    transaction.getReferenceId()
            );

            // Add transaction to history
            transactions.add(transaction);

            System.out.println(
                    "Transaction processed successfully!"
            );

            System.out.println(transaction);
        }


        // Display all transactions
        public void displayTransactions() {

            for(Transaction transaction : transactions) {

                System.out.println(transaction);
            }
        }


        // Display failed transactions
        public void displayFailedTransactions() {

            System.out.println("\nFailed Transactions:");

            for(Map.Entry<String, String> entry :
                    failedTransactions.entrySet()) {

                System.out.println(
                        entry.getKey() +
                                " -> " +
                                entry.getValue()
                );
            }
        }

}
