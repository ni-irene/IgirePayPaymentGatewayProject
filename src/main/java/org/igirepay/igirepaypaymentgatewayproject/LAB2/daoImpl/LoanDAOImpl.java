package org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl;

import org.igirepay.igirepaypaymentgatewayproject.LAB1.model.Loan;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.dao.LoanDAO;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAOImpl implements LoanDAO {

    @Override
    public void createLoan(Loan loan) {

        String sql =
                "INSERT INTO loans(customer_id, savings_account_id, amount, amount_paid, loan_limit, status, taken_at, due_date) " +
                "VALUES(?,?,?,0,?,?,NOW(), NOW() + INTERVAL '30 days')";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, loan.getCustomerId());
            ps.setInt(2, loan.getSavingsAccountId());
            ps.setDouble(3, loan.getAmount());
            ps.setDouble(4, loan.getLoanLimit());
            ps.setString(5, "ACTIVE");
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loan getActiveLoan(int customerId) {

        String sql =
                "SELECT * FROM loans WHERE customer_id=? AND status='ACTIVE' ORDER BY taken_at DESC LIMIT 1";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapLoan(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void repayLoan(int loanId, double amount) {

        // Add to amount_paid, mark PAID if fully settled
        String sql =
                "UPDATE loans SET amount_paid = amount_paid + ?, " +
                "status = CASE WHEN amount_paid + ? >= amount THEN 'PAID' ELSE status END " +
                "WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDouble(1, amount);
            ps.setDouble(2, amount);
            ps.setInt(3, loanId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLoanStatus(int loanId, String status) {

        String sql = "UPDATE loans SET status=? WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, status);
            ps.setInt(2, loanId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Loan> getLoansByCustomer(int customerId) {

        String sql =
                "SELECT * FROM loans WHERE customer_id=? ORDER BY taken_at DESC";

        List<Loan> loans = new ArrayList<>();

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) loans.add(mapLoan(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loans;
    }

    private Loan mapLoan(ResultSet rs) throws SQLException {

        Loan loan = new Loan();
        loan.setId(rs.getInt("id"));
        loan.setCustomerId(rs.getInt("customer_id"));
        loan.setSavingsAccountId(rs.getInt("savings_account_id"));
        loan.setAmount(rs.getDouble("amount"));
        loan.setAmountPaid(rs.getDouble("amount_paid"));
        loan.setLoanLimit(rs.getDouble("loan_limit"));
        loan.setStatus(rs.getString("status"));

        Timestamp takenAt = rs.getTimestamp("taken_at");
        Timestamp dueDate = rs.getTimestamp("due_date");

        if (takenAt != null) loan.setTakenAt(takenAt.toLocalDateTime());
        if (dueDate != null) loan.setDueDate(dueDate.toLocalDateTime());

        return loan;
    }
}
