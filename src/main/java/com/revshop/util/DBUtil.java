package com.revshop.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private static Connection connection;
    private static boolean alreadyPrinted = false;

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "admin";

    private DBUtil() {}

    public static Connection getConnection() {

        try {
            if (connection == null || connection.isClosed()) {

                Class.forName("oracle.jdbc.OracleDriver");

                connection = DriverManager.getConnection(
                        URL,
                        USERNAME,
                        PASSWORD
                );

                // ✅ Print ONLY FIRST TIME
                if (!alreadyPrinted) {
                    System.out.println("✅ Connected to Oracle Database");
                    alreadyPrinted = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
