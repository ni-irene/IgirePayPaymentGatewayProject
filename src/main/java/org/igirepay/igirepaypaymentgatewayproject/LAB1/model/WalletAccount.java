package org.igirepay.igirepaypaymentgatewayproject.LAB1.model;

public class WalletAccount extends Account {

        public WalletAccount() {
        }

        public WalletAccount(int id, double balance) {
            super(id, balance);
        }

        @Override
        public void withdraw(double amount) {

            balance -= amount;

            System.out.println(
                    "Wallet withdrawal successful."
            );
        }

}
