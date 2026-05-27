package org.igirepay.igirepaypaymentgatewayproject.LAB2.dao;

import org.igirepay.igirepaypaymentgatewayproject.LAB1.model.Loan;

public interface LoanDAO {

    void createLoan(Loan loan);

    Loan getActiveLoan(int customerId);

    void repayLoan(int loanId, double amount);

    void updateLoanStatus(int loanId, String status);

    java.util.List<Loan> getLoansByCustomer(int customerId);
}
