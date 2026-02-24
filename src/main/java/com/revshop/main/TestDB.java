package com.revshop.main;

import java.sql.Connection;
import com.revshop.util.DBUtil;

public class TestDB {
    public static void main(String[] args) {
        Connection con = DBUtil.getConnection();

        if (con != null) {
            System.out.println("DATABASE CONNECTED");
        } else {
            System.out.println("DATABASE CONNECTION FAILED");
        }
    }
}
