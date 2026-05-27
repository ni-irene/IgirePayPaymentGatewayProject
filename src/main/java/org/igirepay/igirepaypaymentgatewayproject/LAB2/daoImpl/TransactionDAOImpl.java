package org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl;


import org.igirepay.igirepaypaymentgatewayproject.LAB2.dao.TransactionDAO;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransactionDAOImpl
        implements TransactionDAO {

    @Override
    public void saveTransaction(
            int accountId,
            String referenceId,
            String transactionType,
            double amount
    ) {

        String sql =
                "INSERT INTO transactions(account_id,reference_id,transaction_type,amount) VALUES(?,?,?,?)";

        try(
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, accountId);
            ps.setString(2, referenceId);
            ps.setString(3, transactionType);
            ps.setDouble(4, amount);

            ps.executeUpdate();

            System.out.println(
                    "Transaction Saved Successfully!"
            );

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void viewTransactions() {

        String sql = "SELECT * FROM transactions";

        try(
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while(rs.next()) {

                System.out.println(
                        rs.getInt("id") +
                                " | " +
                                rs.getString("reference_id") +
                                " | " +
                                rs.getString("transaction_type") +
                                " | " +
                                rs.getDouble("amount")
                );
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void viewTransactionsByAccount(int accountId) {

        String sql =
                "SELECT * FROM transactions WHERE account_id=? ORDER BY created_at DESC";

        try(
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                System.out.println(
                        rs.getInt("id") +
                                " | " +
                                rs.getString("reference_id") +
                                " | " +
                                rs.getString("transaction_type") +
                                " | " +
                                rs.getDouble("amount") +
                                " | " +
                                rs.getTimestamp("created_at")
                );
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
