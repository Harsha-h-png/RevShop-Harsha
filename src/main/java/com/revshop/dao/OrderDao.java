package com.revshop.dao;

import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import com.revshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    /* ================= CREATE ORDER ================= */
    public int createOrder(int userId, double totalAmount) {

        String sql = """
            INSERT INTO orders(user_id, total_amount, status, order_date)
            VALUES (?, ?, 'PLACED', SYSTIMESTAMP)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"order_id"})) {

            ps.setInt(1, userId);
            ps.setDouble(2, totalAmount);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /* ================= ADD ORDER ITEM ================= */
    public void addOrderItem(int orderId, int productId, int quantity, double price) {

        String sql = """
            INSERT INTO order_items(order_id, product_id, quantity, price)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= FETCH ORDERS BY USER ================= */
    public List<Order> getOrdersByUser(int userId) {

        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT order_id, user_id, total_amount, status, order_date
            FROM orders
            WHERE user_id = ?
            ORDER BY order_date DESC
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setOrderDate(rs.getTimestamp("order_date"));

                    orders.add(order);
                }
            }

            // ✅ Load items AFTER loop
            for (Order order : orders) {
                order.setItems(getOrderItems(order.getOrderId()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    /* ================= FETCH ORDER ITEMS ================= */
    public List<OrderItem> getOrderItems(int orderId) {

        List<OrderItem> items = new ArrayList<>();

        String sql = """
            SELECT oi.order_item_id,
                   oi.order_id,
                   oi.product_id,
                   oi.quantity,
                   oi.price,
                   p.name
            FROM order_items oi
            JOIN products p ON oi.product_id = p.product_id
            WHERE oi.order_id = ?
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    OrderItem item = new OrderItem();
                    item.setOrderItemId(rs.getInt("order_item_id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getDouble("price"));
                    item.setProductName(rs.getString("name"));

                    items.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    /* ================= GET ORDER BY ID ================= */
    public Order getOrderById(int orderId) {

        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setOrderDate(rs.getTimestamp("order_date"));

                    return order;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* ================= CANCEL ORDER (TRANSACTION SAFE) ================= */
    public boolean cancelOrder(int orderId) {

        String checkSql = "SELECT status FROM orders WHERE order_id = ?";

        String restoreStockSql = """
        UPDATE products p
        SET stock = stock + (
            SELECT oi.quantity
            FROM order_items oi
            WHERE oi.order_id = ?
            AND oi.product_id = p.product_id
        )
        WHERE EXISTS (
            SELECT 1 FROM order_items oi
            WHERE oi.order_id = ?
            AND oi.product_id = p.product_id
        )
    """;

        String cancelSql = "UPDATE orders SET status = 'CANCELLED' WHERE order_id = ?";

        try (Connection con = DBUtil.getConnection()) {

            con.setAutoCommit(false);  // ✅ BEGIN TRANSACTION

            // ✅ Check order status
            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {

                checkPs.setInt(1, orderId);

                try (ResultSet rs = checkPs.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("❌ Order not found");
                        con.rollback();   // ✅ FIX
                        return false;
                    }

                    String status = rs.getString(1);

                    if ("CANCELLED".equalsIgnoreCase(status)) {
                        System.out.println("⚠ Order already cancelled");
                        con.rollback();   // ✅ FIX
                        return false;
                    }

                    if ("DELIVERED".equalsIgnoreCase(status)) {
                        System.out.println("❌ Delivered orders cannot be cancelled");
                        con.rollback();   // ✅ FIX
                        return false;
                    }
                }
            }

            // ✅ Restore stock
            try (PreparedStatement restorePs = con.prepareStatement(restoreStockSql)) {

                restorePs.setInt(1, orderId);
                restorePs.setInt(2, orderId);

                restorePs.executeUpdate();
            }

            // ✅ Cancel order
            try (PreparedStatement cancelPs = con.prepareStatement(cancelSql)) {

                cancelPs.setInt(1, orderId);
                cancelPs.executeUpdate();
            }

            con.commit();   // ✅ COMMIT TRANSACTION

            System.out.println("✅ Order cancelled & stock restored");
            return true;

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("❌ Cancellation failed");

            return false;
        }
    }

    /* ================= GET ORDER AMOUNT ================= */
    public double getOrderAmount(int orderId) {

        String sql = "SELECT total_amount FROM orders WHERE order_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /* ================= GET ORDER DETAILS ================= */
    public Order getOrderDetails(int orderId) {

        Order order = null;

        String sql = """
            SELECT order_id, user_id, total_amount, status, order_date
            FROM orders
            WHERE order_id = ?
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setOrderDate(rs.getTimestamp("order_date"));

                    order.setItems(getOrderItems(orderId));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }
}