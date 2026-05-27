package org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl;

import org.igirepay.igirepaypaymentgatewayproject.LAB1.model.Customer;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.dao.CustomerDAO;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public void addCustomer(Customer customer) {

        String sql =
                "INSERT INTO customers(full_name, email, phone_number, pin, role, locked, failed_attempts) VALUES(?,?,?,?,?,false,0)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getPin());
            ps.setString(5, customer.getRole() != null ? customer.getRole() : "customer");
            ps.executeUpdate();
            System.out.println("Customer Added Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer getCustomerById(int id) {

        String sql = "SELECT * FROM customers WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setFullName(rs.getString("full_name"));
                c.setEmail(rs.getString("email"));
                c.setPhoneNumber(rs.getString("phone_number"));
                c.setPin(rs.getString("pin"));
                c.setRole(rs.getString("role"));
                c.setLocked(rs.getBoolean("locked"));
                c.setFailedAttempts(rs.getInt("failed_attempts"));
                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void viewCustomers() {

        String sql = "SELECT * FROM customers";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("full_name") + " | " +
                        rs.getString("email") + " | " +
                        rs.getString("phone_number") + " | " +
                        rs.getString("role") + " | locked=" +
                        rs.getBoolean("locked")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(Customer customer) {

        String sql =
                "UPDATE customers SET full_name=?, email=?, phone_number=?, pin=?, role=? WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getPin());
            ps.setString(5, customer.getRole());
            ps.setInt(6, customer.getId());
            ps.executeUpdate();
            System.out.println("Customer Updated Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCustomer(int id) {

        String sql = "DELETE FROM customers WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Customer Deleted Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void incrementFailedAttempts(int customerId) {

        String sql =
                "UPDATE customers SET failed_attempts = failed_attempts + 1 WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lockAccount(int customerId) {

        String sql = "UPDATE customers SET locked=true WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
            System.out.println("Account locked for customer: " + customerId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlockAccount(int customerId) {

        String sql = "UPDATE customers SET locked=false, failed_attempts=0 WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
            System.out.println("Account unlocked for customer: " + customerId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetFailedAttempts(int customerId) {

        String sql =
                "UPDATE customers SET failed_attempts=0 WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
