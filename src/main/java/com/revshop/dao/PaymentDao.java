package com.revshop.dao;

import com.revshop.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDao {

    // ✅ CREATE PAYMENT
    public boolean createPayment(int orderId, double amount, String method) {

        String sql = """
            INSERT INTO payments (
                payment_id,
                order_id,
                amount,
                payment_mode,
                payment_method,
                payment_status
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, getNextPaymentId(con));
            ps.setInt(2, orderId);
            ps.setDouble(3, amount);
            ps.setString(4, "PAYMENT");
            ps.setString(5, method);
            ps.setString(6, "SUCCESS");

            int rows = ps.executeUpdate();
            return rows > 0;   // ✅ IMPORTANT

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ✅ CREATE REFUND
    public boolean createRefund(int orderId, double amount) {

        String sql = """
            INSERT INTO payments (
                payment_id,
                order_id,
                amount,
                payment_mode,
                payment_method,
                payment_status
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, getNextPaymentId(con));
            ps.setInt(2, orderId);
            ps.setDouble(3, amount);
            ps.setString(4, "REFUND");
            ps.setString(5, "REFUND");
            ps.setString(6, "SUCCESS");

            int rows = ps.executeUpdate();
            return rows > 0;   // ✅ IMPORTANT

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ✅ PAYMENT_ID generator
    private int getNextPaymentId(Connection con) throws SQLException {

        String sql = "SELECT NVL(MAX(payment_id), 0) + 1 FROM payments";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getInt(1);
        }
    }
}