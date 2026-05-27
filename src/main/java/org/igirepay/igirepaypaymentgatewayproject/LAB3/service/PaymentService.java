package org.igirepay.igirepaypaymentgatewayproject.LAB3.service;

import org.igirepay.igirepaypaymentgatewayproject.LAB1.model.Loan;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.AccountDAOImpl;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.LoanDAOImpl;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.ProcessedRequestDAOImpl;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.TransactionDAOImpl;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.exception.DuplicateTransactionException;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.exception.InsufficientBalanceException;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.exception.InvalidAmountException;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.exception.LoanException;

public class PaymentService {

    private final AccountDAOImpl accountDAO;
    private final TransactionDAOImpl transactionDAO;
    private final ProcessedRequestDAOImpl processedDAO;
    private final LoanDAOImpl loanDAO;

    // Loan limit is 50% of savings balance
    private static final double LOAN_LIMIT_RATIO = 0.50;

    public PaymentService(
            AccountDAOImpl accountDAO,
            TransactionDAOImpl transactionDAO,
            ProcessedRequestDAOImpl processedDAO,
            LoanDAOImpl loanDAO
    ) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
        this.processedDAO = processedDAO;
        this.loanDAO = loanDAO;
    }

    // ===================== GUARDS =====================

    private void checkDuplicate(String referenceId) throws DuplicateTransactionException {
        if (processedDAO.isDuplicateReference(referenceId))
            throw new DuplicateTransactionException(
                    "This transaction was already processed. Reference ID: " + referenceId
            );
    }

    private void checkAmount(double amount) throws InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException("Amount must be greater than zero.");
    }

    private void checkSufficientBalance(int accountId, double amount)
            throws InsufficientBalanceException {
        double balance = accountDAO.checkBalance(accountId);
        if (balance < amount)
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: RWF " + String.format("%.2f", balance)
            );
    }

    // ===================== TRANSACTIONS =====================

    public void deposit(int accountId, double amount, String referenceId)
            throws InvalidAmountException, DuplicateTransactionException {

        checkAmount(amount);
        checkDuplicate(referenceId);

        accountDAO.deposit(accountId, amount);
        transactionDAO.saveTransaction(accountId, referenceId, "Deposit", amount);
        processedDAO.saveReferenceId(referenceId);
    }

    public void withdraw(int accountId, double amount, String referenceId)
            throws InvalidAmountException, DuplicateTransactionException, InsufficientBalanceException {

        checkAmount(amount);
        checkDuplicate(referenceId);
        checkSufficientBalance(accountId, amount);

        accountDAO.withdraw(accountId, amount);
        transactionDAO.saveTransaction(accountId, referenceId, "Withdraw", amount);
        processedDAO.saveReferenceId(referenceId);
    }

    public void transfer(int fromAccountId, int toAccountId, double amount, String referenceId)
            throws InvalidAmountException, DuplicateTransactionException, InsufficientBalanceException {

        checkAmount(amount);
        checkDuplicate(referenceId);
        checkSufficientBalance(fromAccountId, amount);

        accountDAO.transfer(fromAccountId, toAccountId, amount);
        transactionDAO.saveTransaction(fromAccountId, referenceId, "Transfer-Out", amount);
        transactionDAO.saveTransaction(toAccountId, referenceId + "-IN", "Transfer-In", amount);
        processedDAO.saveReferenceId(referenceId);
    }

    public void pushToSavings(int walletAccountId, int savingsAccountId, double amount, String referenceId)
            throws InvalidAmountException, DuplicateTransactionException, InsufficientBalanceException {

        checkAmount(amount);
        checkDuplicate(referenceId);
        checkSufficientBalance(walletAccountId, amount);

        accountDAO.transfer(walletAccountId, savingsAccountId, amount);
        transactionDAO.saveTransaction(walletAccountId, referenceId, "Push-To-Savings", amount);
        transactionDAO.saveTransaction(savingsAccountId, referenceId + "-S", "Savings-Credit", amount);
        processedDAO.saveReferenceId(referenceId);
    }

    public void pullFromSavings(int savingsAccountId, int walletAccountId, double amount, String referenceId)
            throws InvalidAmountException, DuplicateTransactionException, InsufficientBalanceException {

        checkAmount(amount);
        checkDuplicate(referenceId);
        checkSufficientBalance(savingsAccountId, amount);

        accountDAO.transfer(savingsAccountId, walletAccountId, amount);
        transactionDAO.saveTransaction(savingsAccountId, referenceId, "Pull-From-Savings", amount);
        transactionDAO.saveTransaction(walletAccountId, referenceId + "-W", "Savings-Debit", amount);
        processedDAO.saveReferenceId(referenceId);
    }

    public void buyAirtime(int walletAccountId, double amount, String phoneNumber, String referenceId)
            throws InvalidAmountException, DuplicateTransactionException, InsufficientBalanceException {

        checkAmount(amount);
        checkDuplicate(referenceId);
        checkSufficientBalance(walletAccountId, amount);

        accountDAO.withdraw(walletAccountId, amount);
        transactionDAO.saveTransaction(walletAccountId, referenceId, "Airtime-" + phoneNumber, amount);
        processedDAO.saveReferenceId(referenceId);
    }

    // ===================== LOANS =====================

    /**
     * Calculates the maximum loan a customer can take based on savings balance.
     * Loan limit = 50% of savings balance.
     */
    public double getLoanLimit(int savingsAccountId) {
        double savingsBalance = accountDAO.checkBalance(savingsAccountId);
        return savingsBalance * LOAN_LIMIT_RATIO;
    }

    /**
     * Customer takes a loan against their savings account.
     * Loan amount is credited to their wallet account.
     * Rules:
     *  - Cannot have an existing active loan
     *  - Cannot exceed 50% of savings balance
     *  - Must repay within 30 days
     */
    public void takeLoan(
            int customerId,
            int savingsAccountId,
            int walletAccountId,
            double amount,
            String referenceId
    ) throws LoanException, InvalidAmountException, DuplicateTransactionException {

        checkAmount(amount);
        checkDuplicate(referenceId);

        // Check for existing active loan
        Loan existingLoan = loanDAO.getActiveLoan(customerId);
        if (existingLoan != null)
            throw new LoanException(
                    "You already have an active loan of RWF " +
                    String.format("%.2f", existingLoan.getRemainingBalance()) +
                    " remaining. Please repay it before taking a new loan."
            );

        // Check loan limit
        double loanLimit = getLoanLimit(savingsAccountId);
        if (amount > loanLimit)
            throw new LoanException(
                    "Loan amount exceeds your limit. " +
                    "Your maximum loan is RWF " + String.format("%.2f", loanLimit) +
                    " (50% of your savings balance of RWF " +
                    String.format("%.2f", accountDAO.checkBalance(savingsAccountId)) + ")."
            );

        if (loanLimit <= 0)
            throw new LoanException(
                    "You are not eligible for a loan. Your savings account balance is too low."
            );

        // Create loan record
        Loan loan = new Loan(customerId, savingsAccountId, amount, loanLimit);
        loanDAO.createLoan(loan);

        // Credit wallet with loan amount
        accountDAO.deposit(walletAccountId, amount);
        transactionDAO.saveTransaction(walletAccountId, referenceId, "Loan-Disbursed", amount);
        processedDAO.saveReferenceId(referenceId);
    }

    /**
     * Customer repays their active loan.
     * Repayment is deducted from wallet.
     * Rules:
     *  - Must have an active loan
     *  - Cannot overpay (repayment capped at remaining balance)
     */
    public void repayLoan(
            int customerId,
            int walletAccountId,
            double amount,
            String referenceId
    ) throws LoanException, InvalidAmountException, DuplicateTransactionException, InsufficientBalanceException {

        checkAmount(amount);
        checkDuplicate(referenceId);

        Loan activeLoan = loanDAO.getActiveLoan(customerId);
        if (activeLoan == null)
            throw new LoanException("You have no active loan to repay.");

        double remaining = activeLoan.getRemainingBalance();

        // Cap repayment at remaining balance — no overpayment
        double repayAmount = Math.min(amount, remaining);

        checkSufficientBalance(walletAccountId, repayAmount);

        accountDAO.withdraw(walletAccountId, repayAmount);
        loanDAO.repayLoan(activeLoan.getId(), repayAmount);
        transactionDAO.saveTransaction(walletAccountId, referenceId, "Loan-Repayment", repayAmount);
        processedDAO.saveReferenceId(referenceId);
    }
}
