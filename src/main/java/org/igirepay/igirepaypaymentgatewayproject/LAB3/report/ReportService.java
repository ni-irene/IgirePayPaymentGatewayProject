package org.igirepay.igirepaypaymentgatewayproject.LAB3.report;

import org.igirepay.igirepaypaymentgatewayproject.LAB2.util.DBConnection;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportService {

    public void exportTransactionsToCSV(String filePath) {

        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                FileWriter fw = new FileWriter(filePath)
        ) {

            fw.write("ID,Account ID,Reference ID,Type,Amount,Date\n");

            while (rs.next()) {

                fw.write(
                        rs.getInt("id") + "," +
                        rs.getInt("account_id") + "," +
                        rs.getString("reference_id") + "," +
                        rs.getString("transaction_type") + "," +
                        rs.getDouble("amount") + "," +
                        rs.getTimestamp("created_at") + "\n"
                );
            }

            System.out.println("Transactions exported to: " + filePath);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void viewDailySummary() {

        String sql =
                "SELECT DATE(created_at) AS day, transaction_type, " +
                "COUNT(*) AS total_count, SUM(amount) AS total_amount " +
                "FROM transactions " +
                "GROUP BY DATE(created_at), transaction_type " +
                "ORDER BY day DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            System.out.println("\n--- Daily Transaction Summary ---");
            System.out.printf("%-15s %-15s %-10s %-15s%n",
                    "Date", "Type", "Count", "Total Amount");

            while (rs.next()) {

                System.out.printf("%-15s %-15s %-10d %-15.2f%n",
                        rs.getDate("day"),
                        rs.getString("transaction_type"),
                        rs.getInt("total_count"),
                        rs.getDouble("total_amount")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void viewCustomerStatement(int accountId) {

        String sql =
                "SELECT * FROM transactions WHERE account_id=? ORDER BY created_at DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Statement for Account " + accountId + " ---");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-15s %-10.2f %-20s%n",
                        rs.getInt("id"),
                        rs.getString("reference_id"),
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("created_at")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
