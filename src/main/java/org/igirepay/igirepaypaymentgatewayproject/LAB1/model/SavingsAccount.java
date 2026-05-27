package org.igirepay.igirepaypaymentgatewayproject.LAB1.model;

public class SavingsAccount extends Account {

        private final double withdrawalFee = 100;

        public SavingsAccount() {
        }

        public SavingsAccount(int id, double balance) {
            super(id, balance);
        }

        @Override
        public void withdraw(double amount) {

            balance -= (amount + withdrawalFee);

            System.out.println(
                    "Savings withdrawal processed with fee."
            );
        }


}
