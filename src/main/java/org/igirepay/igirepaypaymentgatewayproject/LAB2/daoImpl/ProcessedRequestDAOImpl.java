package org.igirepay.igirepaypaymentgatewayproject.LAB2.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.dao.ProcessedRequestDAO;
import org.igirepay.igirepaypaymentgatewayproject.LAB2.util.DBConnection;

public class ProcessedRequestDAOImpl
        implements ProcessedRequestDAO {

    @Override
    public boolean isDuplicateReference(
            String referenceId
    ) {

        String sql =
                "SELECT * FROM processed_requests WHERE reference_id=?";

        try(
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, referenceId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void saveReferenceId(
            String referenceId
    ) {

        String sql =
                "INSERT INTO processed_requests(reference_id) VALUES(?)";

        try(
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, referenceId);

            ps.executeUpdate();

            System.out.println(
                    "Reference ID Stored Successfully!"
            );

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
