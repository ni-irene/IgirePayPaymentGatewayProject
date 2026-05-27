package org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl;

import org.igirepay.igirepaypaymentgatewayproject.LAB2.dao.AccountDAO;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAOImpl implements AccountDAO {

    @Override
    public void createAccount(int customerId, String accountType, double balance) {

        String sql =
                "INSERT INTO accounts(customer_id, account_type, balance) VALUES(?,?,?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ps.setString(2, accountType);
            ps.setDouble(3, balance);
            ps.executeUpdate();
            System.out.println("Account Created Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean accountTypeExists(int customerId, String accountType) {

        String sql =
                "SELECT id FROM accounts WHERE customer_id=? AND account_type=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ps.setString(2, accountType);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int getAccountIdByCustomerAndType(int customerId, String accountType) {

        String sql =
                "SELECT id FROM accounts WHERE customer_id=? AND account_type=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ps.setString(2, accountType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public void viewAccounts() {

        String sql = "SELECT * FROM accounts";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("account_type") + " | Balance: " +
                        rs.getDouble("balance")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewAccountsByCustomer(int customerId) {

        String sql = "SELECT * FROM accounts WHERE customer_id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("account_type") + " | Balance: " +
                        rs.getDouble("balance")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deposit(int accountId, double amount) {

        String sql = "UPDATE accounts SET balance = balance + ? WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void withdraw(int accountId, double amount) {

        String sql = "UPDATE accounts SET balance = balance - ? WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public double checkBalance(int accountId) {

        String sql = "SELECT balance FROM accounts WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("balance");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void deleteAccount(int accountId) {

        String sql = "DELETE FROM accounts WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, accountId);
            ps.executeUpdate();
            System.out.println("Account Deleted Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transfer(int fromAccountId, int toAccountId, double amount) {

        String debit  = "UPDATE accounts SET balance = balance - ? WHERE id=?";
        String credit = "UPDATE accounts SET balance = balance + ? WHERE id=?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (
                    PreparedStatement ps1 = con.prepareStatement(debit);
                    PreparedStatement ps2 = con.prepareStatement(credit)
            ) {
                ps1.setDouble(1, amount); ps1.setInt(2, fromAccountId); ps1.executeUpdate();
                ps2.setDouble(1, amount); ps2.setInt(2, toAccountId);   ps2.executeUpdate();
                con.commit();

            } catch (Exception e) {
                con.rollback();
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
