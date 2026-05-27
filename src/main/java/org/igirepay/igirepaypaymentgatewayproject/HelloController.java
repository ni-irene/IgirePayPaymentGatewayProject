package org.igirepay.igirepaypaymentgatewayproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.igirepay.igirepaypaymentgatewayproject.LAB1.model.Customer;
import org.igirepay.igirepaypaymentgatewayproject.LAB1.model.Loan;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl.*;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.util.DBConnection;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.exception.*;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.report.ReportService;
import org.igirepay.igirepaypaymentgatewayproject.LAB3.service.PaymentService;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.UUID;

public class HelloController implements Initializable {

    // --- DAOs & Services ---
    private final CustomerDAOImpl customerDAO = new CustomerDAOImpl();
    private final AccountDAOImpl accountDAO = new AccountDAOImpl();
    private final TransactionDAOImpl transactionDAO = new TransactionDAOImpl();
    private final ProcessedRequestDAOImpl processedDAO = new ProcessedRequestDAOImpl();
    private final LoanDAOImpl loanDAO = new LoanDAOImpl();
    private final PaymentService paymentService = new PaymentService(accountDAO, transactionDAO, processedDAO, loanDAO);
    private final ReportService reportService = new ReportService();

    // Logged-in customer
    private Customer loggedInCustomer;

    // ===================== LOGIN SCREEN =====================
    @FXML private javafx.scene.layout.VBox loginScreen;
    @FXML private javafx.scene.layout.BorderPane customerScreen;
    @FXML private javafx.scene.layout.BorderPane adminScreen;

    @FXML private TextField loginIdField;
    @FXML private PasswordField loginPinField;
    @FXML private Label loginMessage;

    // ===================== CUSTOMER DASHBOARD =====================
    @FXML private Label customerWelcomeLabel;

    // My Accounts
    @FXML private Label walletBalanceLabel;
    @FXML private Label savingsBalanceLabel;
    @FXML private TableView<ObservableList<String>> myAccountsTable;
    @FXML private TableColumn<ObservableList<String>, String> myAccId, myAccType, myAccBalance, myAccCreated;

    // Deposit
    @FXML private TextField depAmountField, depRefField;
    @FXML private Label depMessage;

    // Withdraw
    @FXML private TextField withAmountField, withRefField;
    @FXML private Label withMessage;

    // Transfer
    @FXML private TextField transferToCustomerIdField, transferAmountField, transferRefField;
    @FXML private Label transferMessage;

    // Savings
    @FXML private TextField savingsAmountField, savingsRefField;
    @FXML private Label savingsMessage;

    // Airtime
    @FXML private TextField airtimePhoneField, airtimeAmountField, airtimeRefField;
    @FXML private Label airtimeMessage;

    // My Transactions
    @FXML private TableView<ObservableList<String>> myTransactionsTable;
    @FXML private TableColumn<ObservableList<String>, String> myTxId, myTxAccountId, myTxRef, myTxType, myTxAmount, myTxDate;

    // Loan
    @FXML private Label loanLimitLabel, activeLoanAmountLabel, activeLoanPaidLabel, activeLoanRemainingLabel, activeLoanDueLabel;
    @FXML private TextField loanAmountField, loanRefField, loanRepayAmountField, loanRepayRefField;
    @FXML private Label loanMessage;

    // Change PIN (customer)
    @FXML private PasswordField custCurrentPinField, custNewPinField;
    @FXML private Label custPinMessage;

    // ===================== ADMIN DASHBOARD =====================
    @FXML private Label adminStatusLabel;

    // Admin - Customers
    @FXML private TextField adminCustNameField, adminCustEmailField, adminCustPhoneField, adminCustIdField;
    @FXML private PasswordField adminCustPinField;
    @FXML private ComboBox<String> adminRoleCombo;
    @FXML private Label adminCustMessage;
    @FXML private TableView<ObservableList<String>> adminCustomersTable;
    @FXML private TableColumn<ObservableList<String>, String> adminColCustId, adminColCustName, adminColCustEmail, adminColCustPhone, adminColCustRole, adminColCustLocked;

    // Admin - Accounts
    @FXML private TextField adminAccCustIdField, adminAccBalanceField, adminAccIdField;
    @FXML private ComboBox<String> adminAccTypeCombo;
    @FXML private Label adminAccMessage;
    @FXML private TableView<ObservableList<String>> adminAccountsTable;
    @FXML private TableColumn<ObservableList<String>, String> adminColAccId, adminColAccCustId, adminColAccType, adminColAccBalance, adminColAccCreated;

    // Admin - Transactions
    @FXML private TableView<ObservableList<String>> adminTransactionsTable;
    @FXML private TableColumn<ObservableList<String>, String> adminColTxId, adminColTxAccId, adminColTxRef, adminColTxType, adminColTxAmount, adminColTxDate;

    // Admin - Reports
    @FXML private TextField adminCsvPathField;
    @FXML private TextArea adminReportArea;

    // ===================== INITIALIZE =====================

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adminRoleCombo.setItems(FXCollections.observableArrayList("customer", "admin"));
        adminRoleCombo.getSelectionModel().selectFirst();
        adminAccTypeCombo.setItems(FXCollections.observableArrayList("Wallet", "Savings"));
        adminAccTypeCombo.getSelectionModel().selectFirst();
        bindColumns();
    }

    // ===================== LOGIN / LOGOUT =====================

    @FXML
    public void onLogin() {
        String idText = loginIdField.getText().trim();
        String pin = loginPinField.getText().trim();

        if (idText.isEmpty() || pin.isEmpty()) {
            loginMessage.setText("Please enter Customer ID and PIN.");
            return;
        }

        try {
            Customer customer = customerDAO.getCustomerById(Integer.parseInt(idText));

            if (customer == null) {
                loginMessage.setStyle("-fx-text-fill: red;");
                loginMessage.setText("Customer not found.");
                return;
            }

            if (customer.isLocked()) {
                loginMessage.setStyle("-fx-text-fill: red;");
                loginMessage.setText("Account is locked. Contact admin.");
                return;
            }

            if (!pin.equals(customer.getPin())) {
                customerDAO.incrementFailedAttempts(customer.getId());
                if (customer.getFailedAttempts() + 1 >= 3) customerDAO.lockAccount(customer.getId());
                loginMessage.setStyle("-fx-text-fill: red;");
                loginMessage.setText("Incorrect PIN.");
                return;
            }

            customerDAO.resetFailedAttempts(customer.getId());
            loggedInCustomer = customer;

            loginScreen.setVisible(false);

            if ("admin".equals(customer.getRole())) {
                adminScreen.setVisible(true);
                adminStatusLabel.setText("Admin: " + customer.getFullName());
                loadAdminTables();
            } else {
                customerScreen.setVisible(true);
                customerWelcomeLabel.setText("Welcome, " + customer.getFullName());
                loadCustomerData();
            }

        } catch (NumberFormatException e) {
            loginMessage.setText("Customer ID must be a number.");
        }
    }

    @FXML
    public void onLogout() {
        loggedInCustomer = null;
        loginScreen.setVisible(true);
        customerScreen.setVisible(false);
        adminScreen.setVisible(false);
        loginIdField.clear();
        loginPinField.clear();
        loginMessage.setText("");
    }

    // ===================== CUSTOMER - DEPOSIT =====================

    @FXML
    public void onGenDepRef() { depRefField.setText(generateRef()); }

    @FXML
    public void onDeposit() {
        try {
            int walletId = getWalletId();
            double amount = Double.parseDouble(depAmountField.getText().trim());
            String ref = depRefField.getText().trim();
            paymentService.deposit(walletId, amount, ref);
            depMessage.setStyle("-fx-text-fill: green;");
            depMessage.setText("Deposit successful!");
            depAmountField.clear(); depRefField.clear();
            loadCustomerData();
        } catch (Exception e) {
            depMessage.setStyle("-fx-text-fill: red;");
            depMessage.setText("Error: " + e.getMessage());
        }
    }

    // ===================== CUSTOMER - WITHDRAW =====================

    @FXML
    public void onGenWithRef() { withRefField.setText(generateRef()); }

    @FXML
    public void onWithdraw() {
        try {
            int walletId = getWalletId();
            double amount = Double.parseDouble(withAmountField.getText().trim());
            String ref = withRefField.getText().trim();
            paymentService.withdraw(walletId, amount, ref);
            withMessage.setStyle("-fx-text-fill: green;");
            withMessage.setText("Withdrawal successful!");
            withAmountField.clear(); withRefField.clear();
            loadCustomerData();
        } catch (Exception e) {
            withMessage.setStyle("-fx-text-fill: red;");
            withMessage.setText("Error: " + e.getMessage());
        }
    }

    // ===================== CUSTOMER - TRANSFER =====================

    @FXML
    public void onGenTransferRef() { transferRefField.setText(generateRef()); }

    @FXML
    public void onTransfer() {
        try {
            int fromWalletId = getWalletId();
            int toCustomerId = Integer.parseInt(transferToCustomerIdField.getText().trim());
            int toWalletId = accountDAO.getAccountIdByCustomerAndType(toCustomerId, "Wallet");
            if (toWalletId == -1) {
                transferMessage.setStyle("-fx-text-fill: red;");
                transferMessage.setText("Recipient has no Wallet account.");
                return;
            }
            double amount = Double.parseDouble(transferAmountField.getText().trim());
            String ref = transferRefField.getText().trim();
            paymentService.transfer(fromWalletId, toWalletId, amount, ref);
            transferMessage.setStyle("-fx-text-fill: green;");
            transferMessage.setText("Transfer successful!");
            transferToCustomerIdField.clear(); transferAmountField.clear(); transferRefField.clear();
            loadCustomerData();
        } catch (Exception e) {
            transferMessage.setStyle("-fx-text-fill: red;");
            transferMessage.setText("Error: " + e.getMessage());
        }
    }

    // ===================== CUSTOMER - SAVINGS =====================

    @FXML
    public void onGenSavingsRef() { savingsRefField.setText(generateRef()); }

    @FXML
    public void onPushToSavings() {
        try {
            int walletId = getWalletId();
            int savingsId = getSavingsId();
            double amount = Double.parseDouble(savingsAmountField.getText().trim());
            String ref = savingsRefField.getText().trim();
            paymentService.pushToSavings(walletId, savingsId, amount, ref);
            savingsMessage.setStyle("-fx-text-fill: green;");
            savingsMessage.setText("Pushed to savings successfully!");
            savingsAmountField.clear(); savingsRefField.clear();
            loadCustomerData();
        } catch (Exception e) {
            savingsMessage.setStyle("-fx-text-fill: red;");
            savingsMessage.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void onPullFromSavings() {
        try {
            int walletId = getWalletId();
            int savingsId = getSavingsId();
            double amount = Double.parseDouble(savingsAmountField.getText().trim());
            String ref = savingsRefField.getText().trim();
            paymentService.pullFromSavings(savingsId, walletId, amount, ref);
            savingsMessage.setStyle("-fx-text-fill: green;");
            savingsMessage.setText("Pulled from savings successfully!");
            savingsAmountField.clear(); savingsRefField.clear();
            loadCustomerData();
        } catch (Exception e) {
            savingsMessage.setStyle("-fx-text-fill: red;");
            savingsMessage.setText("Error: " + e.getMessage());
        }
    }

    // ===================== CUSTOMER - AIRTIME =====================

    @FXML
    public void onGenAirtimeRef() { airtimeRefField.setText(generateRef()); }

    @FXML
    public void onBuyAirtime() {
        try {
            int walletId = getWalletId();
            String phone = airtimePhoneField.getText().trim();
            double amount = Double.parseDouble(airtimeAmountField.getText().trim());
            String ref = airtimeRefField.getText().trim();
            paymentService.buyAirtime(walletId, amount, phone, ref);
            airtimeMessage.setStyle("-fx-text-fill: green;");
            airtimeMessage.setText("Airtime purchased for " + phone + "!");
            airtimePhoneField.clear(); airtimeAmountField.clear(); airtimeRefField.clear();
            loadCustomerData();
        } catch (Exception e) {
            airtimeMessage.setStyle("-fx-text-fill: red;");
            airtimeMessage.setText("Error: " + e.getMessage());
        }
    }

    // ===================== CUSTOMER - LOAN =====================

    @FXML
    public void onCheckLoanLimit() {
        int savingsId = getSavingsId();
        if (savingsId == -1) { loanLimitLabel.setText("No savings account."); return; }
        double limit = paymentService.getLoanLimit(savingsId);
        loanLimitLabel.setText("RWF " + String.format("%.2f", limit));

        Loan active = loanDAO.getActiveLoan(loggedInCustomer.getId());
        if (active != null) {
            activeLoanAmountLabel.setText("Loan Amount: RWF " + String.format("%.2f", active.getAmount()));
            activeLoanPaidLabel.setText("Amount Paid: RWF " + String.format("%.2f", active.getAmountPaid()));
            activeLoanRemainingLabel.setText("Remaining: RWF " + String.format("%.2f", active.getRemainingBalance()));
            activeLoanDueLabel.setText("Due Date: " + active.getDueDate());
        } else {
            activeLoanAmountLabel.setText("No active loan.");
            activeLoanPaidLabel.setText("");
            activeLoanRemainingLabel.setText("");
            activeLoanDueLabel.setText("");
        }
    }

    @FXML
    public void onGenLoanRef() { loanRefField.setText(generateRef()); }

    @FXML
    public void onTakeLoan() {
        try {
            int savingsId = getSavingsId();
            int walletId = getWalletId();
            double amount = Double.parseDouble(loanAmountField.getText().trim());
            String ref = loanRefField.getText().trim();
            paymentService.takeLoan(loggedInCustomer.getId(), savingsId, walletId, amount, ref);
            loanMessage.setStyle("-fx-text-fill: green;");
            loanMessage.setText("Loan of RWF " + String.format("%.2f", amount) + " disbursed to your wallet!");
            loanAmountField.clear(); loanRefField.clear();
            loadCustomerData();
        } catch (Exception e) {
            loanMessage.setStyle("-fx-text-fill: red;");
            loanMessage.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void onGenRepayRef() { loanRepayRefField.setText(generateRef()); }

    @FXML
    public void onRepayLoan() {
        try {
            int walletId = getWalletId();
            double amount = Double.parseDouble(loanRepayAmountField.getText().trim());
            String ref = loanRepayRefField.getText().trim();
            paymentService.repayLoan(loggedInCustomer.getId(), walletId, amount, ref);
            loanMessage.setStyle("-fx-text-fill: green;");
            loanMessage.setText("Repayment of RWF " + String.format("%.2f", amount) + " successful!");
            loanRepayAmountField.clear(); loanRepayRefField.clear();
            loadCustomerData();
        } catch (Exception e) {
            loanMessage.setStyle("-fx-text-fill: red;");
            loanMessage.setText("Error: " + e.getMessage());
        }
    }

    // ===================== CUSTOMER - CHANGE PIN =====================

    @FXML
    public void onCustomerChangePIN() {
        String current = custCurrentPinField.getText().trim();
        String newPin = custNewPinField.getText().trim();

        if (!current.equals(loggedInCustomer.getPin())) {
            custPinMessage.setStyle("-fx-text-fill: red;");
            custPinMessage.setText("Incorrect current PIN.");
            return;
        }

        loggedInCustomer.setPin(newPin);
        customerDAO.updateCustomer(loggedInCustomer);
        custPinMessage.setStyle("-fx-text-fill: green;");
        custPinMessage.setText("PIN changed successfully!");
        custCurrentPinField.clear(); custNewPinField.clear();
    }

    // ===================== ADMIN - CUSTOMERS =====================

    @FXML
    public void onAdminRegisterCustomer() {
        Customer c = new Customer();
        c.setFullName(adminCustNameField.getText().trim());
        c.setEmail(adminCustEmailField.getText().trim());
        c.setPhoneNumber(adminCustPhoneField.getText().trim());
        c.setPin(adminCustPinField.getText().trim());
        c.setRole(adminRoleCombo.getValue());
        customerDAO.addCustomer(c);
        adminCustMessage.setStyle("-fx-text-fill: green;");
        adminCustMessage.setText("Customer registered.");
        clearAdminCustForm();
        loadAdminTables();
    }

    @FXML
    public void onAdminUpdateCustomer() {
        String idText = adminCustIdField.getText().trim();
        if (idText.isEmpty()) { adminCustMessage.setText("Enter Customer ID."); return; }
        Customer existing = customerDAO.getCustomerById(Integer.parseInt(idText));
        if (existing == null) { adminCustMessage.setText("Customer not found."); return; }
        if (!adminCustNameField.getText().isEmpty()) existing.setFullName(adminCustNameField.getText().trim());
        if (!adminCustEmailField.getText().isEmpty()) existing.setEmail(adminCustEmailField.getText().trim());
        if (!adminCustPhoneField.getText().isEmpty()) existing.setPhoneNumber(adminCustPhoneField.getText().trim());
        if (!adminCustPinField.getText().isEmpty()) existing.setPin(adminCustPinField.getText().trim());
        existing.setRole(adminRoleCombo.getValue());
        customerDAO.updateCustomer(existing);
        adminCustMessage.setStyle("-fx-text-fill: green;");
        adminCustMessage.setText("Customer updated.");
        clearAdminCustForm();
        loadAdminTables();
    }

    @FXML
    public void onAdminDeleteCustomer() {
        String idText = adminCustIdField.getText().trim();
        if (idText.isEmpty()) { adminCustMessage.setText("Enter Customer ID."); return; }
        customerDAO.deleteCustomer(Integer.parseInt(idText));
        adminCustMessage.setStyle("-fx-text-fill: green;");
        adminCustMessage.setText("Customer deleted.");
        clearAdminCustForm();
        loadAdminTables();
    }

    @FXML
    public void onAdminLockCustomer() {
        String idText = adminCustIdField.getText().trim();
        if (idText.isEmpty()) { adminCustMessage.setText("Enter Customer ID."); return; }
        customerDAO.lockAccount(Integer.parseInt(idText));
        adminCustMessage.setStyle("-fx-text-fill: orange;");
        adminCustMessage.setText("Customer account locked.");
        loadAdminTables();
    }

    @FXML
    public void onAdminUnlockCustomer() {
        String idText = adminCustIdField.getText().trim();
        if (idText.isEmpty()) { adminCustMessage.setText("Enter Customer ID."); return; }
        // Unlock = reset locked flag and failed attempts
        String sql = "UPDATE customers SET locked=false, failed_attempts=0 WHERE id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idText));
            ps.executeUpdate();
            adminCustMessage.setStyle("-fx-text-fill: green;");
            adminCustMessage.setText("Customer account unlocked.");
        } catch (Exception e) {
            adminCustMessage.setText("Error: " + e.getMessage());
        }
        loadAdminTables();
    }

    // ===================== ADMIN - ACCOUNTS =====================

    @FXML
    public void onAdminCreateAccount() {
        String custIdText = adminAccCustIdField.getText().trim();
        String balanceText = adminAccBalanceField.getText().trim();
        if (custIdText.isEmpty() || balanceText.isEmpty()) {
            adminAccMessage.setText("Customer ID and Balance are required.");
            return;
        }
        accountDAO.createAccount(Integer.parseInt(custIdText), adminAccTypeCombo.getValue(), Double.parseDouble(balanceText));
        adminAccMessage.setStyle("-fx-text-fill: green;");
        adminAccMessage.setText("Account created.");
        adminAccCustIdField.clear(); adminAccBalanceField.clear();
        loadAdminTables();
    }

    @FXML
    public void onAdminDeleteAccount() {
        String idText = adminAccIdField.getText().trim();
        if (idText.isEmpty()) { adminAccMessage.setText("Enter Account ID."); return; }
        accountDAO.deleteAccount(Integer.parseInt(idText));
        adminAccMessage.setStyle("-fx-text-fill: green;");
        adminAccMessage.setText("Account deleted.");
        adminAccIdField.clear();
        loadAdminTables();
    }

    // ===================== ADMIN - REPORTS =====================

    @FXML
    public void onAdminExportCSV() {
        String path = adminCsvPathField.getText().trim();
        if (path.isEmpty()) { adminReportArea.setText("Enter a file path."); return; }
        reportService.exportTransactionsToCSV(path);
        adminReportArea.setText("Exported to: " + path);
    }

    @FXML
    public void onAdminDailySummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %-20s %-10s %-15s%n", "Date", "Type", "Count", "Total (RWF)"));
        sb.append("-".repeat(62)).append("\n");
        String sql = "SELECT DATE(created_at) AS day, transaction_type, COUNT(*) AS cnt, SUM(amount) AS total " +
                     "FROM transactions GROUP BY DATE(created_at), transaction_type ORDER BY day DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                sb.append(String.format("%-15s %-20s %-10d %-15.2f%n",
                        rs.getDate("day"), rs.getString("transaction_type"),
                        rs.getInt("cnt"), rs.getDouble("total")));
        } catch (Exception e) { sb.append("Error: ").append(e.getMessage()); }
        adminReportArea.setText(sb.toString());
    }

    // ===================== HELPERS =====================

    private String generateRef() {
        return "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private int getWalletId() {
        return accountDAO.getAccountIdByCustomerAndType(loggedInCustomer.getId(), "Wallet");
    }

    private int getSavingsId() {
        return accountDAO.getAccountIdByCustomerAndType(loggedInCustomer.getId(), "Savings");
    }

    private void clearAdminCustForm() {
        adminCustNameField.clear(); adminCustEmailField.clear();
        adminCustPhoneField.clear(); adminCustPinField.clear(); adminCustIdField.clear();
    }

    private void loadCustomerData() {
        // Load my accounts table
        myAccountsTable.setItems(queryTable(
                "SELECT id, account_type, balance, created_at FROM accounts WHERE customer_id=" + loggedInCustomer.getId(), 4));

        // Load my transactions table
        myTransactionsTable.setItems(queryTable(
                "SELECT t.id, t.account_id, t.reference_id, t.transaction_type, t.amount, t.created_at " +
                "FROM transactions t JOIN accounts a ON t.account_id = a.id " +
                "WHERE a.customer_id=" + loggedInCustomer.getId() + " ORDER BY t.created_at DESC", 6));

        // Update balance labels
        int walletId = getWalletId();
        int savingsId = getSavingsId();
        walletBalanceLabel.setText("Wallet: RWF " + (walletId != -1 ? String.format("%.2f", accountDAO.checkBalance(walletId)) : "N/A"));
        savingsBalanceLabel.setText("  |  Savings: RWF " + (savingsId != -1 ? String.format("%.2f", accountDAO.checkBalance(savingsId)) : "N/A"));
    }

    private void loadAdminTables() {
        adminCustomersTable.setItems(queryTable(
                "SELECT id, full_name, email, phone_number, role, locked::text FROM customers ORDER BY id DESC", 6));
        adminAccountsTable.setItems(queryTable(
                "SELECT id, customer_id, account_type, balance, created_at FROM accounts ORDER BY id DESC", 5));
        adminTransactionsTable.setItems(queryTable(
                "SELECT id, account_id, reference_id, transaction_type, amount, created_at FROM transactions ORDER BY created_at DESC", 6));
    }

    private ObservableList<ObservableList<String>> queryTable(String sql, int cols) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= cols; i++) {
                    String val = rs.getString(i);
                    row.add(val != null ? val : "");
                }
                data.add(row);
            }
        } catch (Exception e) {
            System.out.println("Table load error: " + e.getMessage());
        }
        return data;
    }

    private void bindColumns() {
        // My Accounts
        bind(myAccId, 0); bind(myAccType, 1); bind(myAccBalance, 2); bind(myAccCreated, 3);

        // My Transactions
        bind(myTxId, 0); bind(myTxAccountId, 1); bind(myTxRef, 2);
        bind(myTxType, 3); bind(myTxAmount, 4); bind(myTxDate, 5);

        // Admin Customers
        bind(adminColCustId, 0); bind(adminColCustName, 1); bind(adminColCustEmail, 2);
        bind(adminColCustPhone, 3); bind(adminColCustRole, 4); bind(adminColCustLocked, 5);

        // Admin Accounts
        bind(adminColAccId, 0); bind(adminColAccCustId, 1); bind(adminColAccType, 2);
        bind(adminColAccBalance, 3); bind(adminColAccCreated, 4);

        // Admin Transactions
        bind(adminColTxId, 0); bind(adminColTxAccId, 1); bind(adminColTxRef, 2);
        bind(adminColTxType, 3); bind(adminColTxAmount, 4); bind(adminColTxDate, 5);
    }

    private void bind(TableColumn<ObservableList<String>, String> col, int index) {
        col.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().get(index)));
    }
}
