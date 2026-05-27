package org.igirepay.igirepaypaymentgatewayproject;

import org.igirepay.igirepaypaymentgatewayproject.LAB1.model.Customer;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.AccountDAOImpl;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.CustomerDAOImpl;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.LoanDAOImpl;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.ProcessedRequestDAOImpl;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.TransactionDAOImpl;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.exception.DuplicateTransactionException;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.exception.InsufficientBalanceException;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.exception.InvalidAmountException;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.report.ReportService;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.service.PaymentService;

import java.util.Scanner;

public class Main {

    static final CustomerDAOImpl customerDAO = new CustomerDAOImpl();
    static final AccountDAOImpl accountDAO = new AccountDAOImpl();
    static final TransactionDAOImpl transactionDAO = new TransactionDAOImpl();
    static final ProcessedRequestDAOImpl processedDAO = new ProcessedRequestDAOImpl();
    static final LoanDAOImpl loanDAO = new LoanDAOImpl();
    static final PaymentService paymentService = new PaymentService(accountDAO, transactionDAO, processedDAO, loanDAO);
    static final ReportService reportService = new ReportService();
    static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        if (!authenticate()) {
            System.out.println("Authentication failed. Exiting.");
            return;
        }

        int choice;

        do {

            System.out.println("\n========== IGIREPAY SYSTEM ==========");
            System.out.println("--- Customer Management ---");
            System.out.println("1.  Register Customer");
            System.out.println("2.  View Customers");
            System.out.println("3.  Update Customer");
            System.out.println("--- Account Management ---");
            System.out.println("4.  Create Account");
            System.out.println("5.  View Accounts");
            System.out.println("6.  View Balance");
            System.out.println("7.  Delete Account");
            System.out.println("--- Transaction Management ---");
            System.out.println("8.  Deposit Money");
            System.out.println("9.  Withdraw Money");
            System.out.println("10. Transfer Money");
            System.out.println("11. View All Transactions");
            System.out.println("12. View Account Statement");
            System.out.println("--- Reports ---");
            System.out.println("13. Export Transactions to CSV");
            System.out.println("14. Daily Transaction Summary");
            System.out.println("--- Auth ---");
            System.out.println("15. Change PIN");
            System.out.println("0.  Exit");
            System.out.print("Choose Option: ");

            choice = input.nextInt();

            switch (choice) {

                case 1:
                    registerCustomer();
                    break;

                case 2:
                    customerDAO.viewCustomers();
                    break;

                case 3:
                    updateCustomer();
                    break;

                case 4:
                    createAccount();
                    break;

                case 5:
                    accountDAO.viewAccounts();
                    break;

                case 6:
                    viewBalance();
                    break;

                case 7:
                    deleteAccount();
                    break;

                case 8:
                    depositMoney();
                    break;

                case 9:
                    withdrawMoney();
                    break;

                case 10:
                    transferMoney();
                    break;

                case 11:
                    transactionDAO.viewTransactions();
                    break;

                case 12:
                    viewAccountStatement();
                    break;

                case 13:
                    exportCSV();
                    break;

                case 14:
                    reportService.viewDailySummary();
                    break;

                case 15:
                    changePIN();
                    break;

                case 0:
                    System.out.println("Thank You For Using IgirePay!");
                    break;

                default:
                    System.out.println("Invalid Option!");
            }

        } while (choice != 0);
    }

    // --- Authentication ---

    private static boolean authenticate() {

        System.out.print("Enter Customer ID: ");
        int id = input.nextInt();
        input.nextLine();

        Customer customer = customerDAO.getCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found.");
            return false;
        }

        System.out.print("Enter PIN: ");
        String pin = input.nextLine();

        if (!pin.equals(customer.getPin())) {
            System.out.println("Incorrect PIN.");
            return false;
        }

        System.out.println("Welcome, " + customer.getFullName() + "!");
        return true;
    }

    private static void changePIN() {

        System.out.print("Enter Customer ID: ");
        int id = input.nextInt();
        input.nextLine();

        Customer customer = customerDAO.getCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.print("Enter Current PIN: ");
        String currentPin = input.nextLine();

        if (!currentPin.equals(customer.getPin())) {
            System.out.println("Incorrect PIN.");
            return;
        }

        System.out.print("Enter New PIN: ");
        String newPin = input.nextLine();

        customer.setPin(newPin);
        customerDAO.updateCustomer(customer);
        System.out.println("PIN changed successfully!");
    }

    // --- Customer ---

    private static void registerCustomer() {

        input.nextLine();

        System.out.print("Enter Full Name: ");
        String fullName = input.nextLine();

        System.out.print("Enter Email: ");
        String email = input.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = input.nextLine();

        System.out.print("Create PIN: ");
        String pin = input.nextLine();

        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setPhoneNumber(phone);
        customer.setPin(pin);

        customerDAO.addCustomer(customer);
    }

    private static void updateCustomer() {

        System.out.print("Enter Customer ID: ");
        int id = input.nextInt();
        input.nextLine();

        Customer customer = customerDAO.getCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.print("New Full Name (" + customer.getFullName() + "): ");
        String fullName = input.nextLine();

        System.out.print("New Email (" + customer.getEmail() + "): ");
        String email = input.nextLine();

        System.out.print("New Phone (" + customer.getPhoneNumber() + "): ");
        String phone = input.nextLine();

        if (!fullName.isEmpty()) customer.setFullName(fullName);
        if (!email.isEmpty()) customer.setEmail(email);
        if (!phone.isEmpty()) customer.setPhoneNumber(phone);

        customerDAO.updateCustomer(customer);
    }

    // --- Account ---

    private static void createAccount() {

        System.out.print("Enter Customer ID: ");
        int customerId = input.nextInt();
        input.nextLine();

        System.out.print("Enter Account Type (Wallet/Savings): ");
        String accountType = input.nextLine();

        System.out.print("Enter Initial Balance: ");
        double balance = input.nextDouble();

        accountDAO.createAccount(customerId, accountType, balance);
    }

    private static void viewBalance() {

        System.out.print("Enter Account ID: ");
        int accountId = input.nextInt();

        double balance = accountDAO.checkBalance(accountId);
        System.out.println("Current Balance: " + balance);
    }

    private static void deleteAccount() {

        System.out.print("Enter Account ID to Delete: ");
        int accountId = input.nextInt();

        accountDAO.deleteAccount(accountId);
    }

    // --- Transactions ---

    private static void depositMoney() {

        System.out.print("Enter Account ID: ");
        int accountId = input.nextInt();

        System.out.print("Enter Deposit Amount: ");
        double amount = input.nextDouble();
        input.nextLine();

        System.out.print("Enter Reference ID: ");
        String referenceId = input.nextLine();

        try {

            paymentService.deposit(accountId, amount, referenceId);
            System.out.println("Deposit Successful!");

        } catch (InvalidAmountException | DuplicateTransactionException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void withdrawMoney() {

        System.out.print("Enter Account ID: ");
        int accountId = input.nextInt();

        System.out.print("Enter Withdrawal Amount: ");
        double amount = input.nextDouble();
        input.nextLine();

        System.out.print("Enter Reference ID: ");
        String referenceId = input.nextLine();

        try {

            paymentService.withdraw(accountId, amount, referenceId);
            System.out.println("Withdrawal Successful!");

        } catch (InvalidAmountException | DuplicateTransactionException | InsufficientBalanceException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void transferMoney() {

        System.out.print("Enter From Account ID: ");
        int fromId = input.nextInt();

        System.out.print("Enter To Account ID: ");
        int toId = input.nextInt();

        System.out.print("Enter Transfer Amount: ");
        double amount = input.nextDouble();
        input.nextLine();

        System.out.print("Enter Reference ID: ");
        String referenceId = input.nextLine();

        try {

            paymentService.transfer(fromId, toId, amount, referenceId);
            System.out.println("Transfer Successful!");

        } catch (InvalidAmountException | DuplicateTransactionException | InsufficientBalanceException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAccountStatement() {

        System.out.print("Enter Account ID: ");
        int accountId = input.nextInt();

        reportService.viewCustomerStatement(accountId);
    }

    // --- Reports ---

    private static void exportCSV() {

        input.nextLine();
        System.out.print("Enter file path (e.g. transactions.csv): ");
        String path = input.nextLine();

        reportService.exportTransactionsToCSV(path);
    }
}
