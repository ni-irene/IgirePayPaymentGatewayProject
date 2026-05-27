package org.igirepay.igirepaypaymentgatewayproject.LAB2.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {


        private static final String URL =
                "jdbc:postgresql://localhost:5432/igirepay";

        private static final String USER =
                "postgres";

        private static final String PASSWORD =
                "admin";

        public static Connection getConnection() {

            try {

                Connection con =
                        DriverManager.getConnection(
                                URL,
                                USER,
                                PASSWORD
                        );

                System.out.println(
                        "Database Connected Successfully!"
                );

                return con;

            } catch (Exception e) {

                System.out.println(
                        "Database Connection Failed!"
                );

                e.printStackTrace();
            }

            return null;
        }

}
