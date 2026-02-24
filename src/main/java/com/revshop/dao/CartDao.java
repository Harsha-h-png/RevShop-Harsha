package com.revshop.dao;

import com.revshop.model.CartItem;
import com.revshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDao {

    /* ================= GET OR CREATE CART ================= */
    public int getOrCreateCart(int userId) {

        String checkSql = "SELECT cart_id FROM carts WHERE user_id = ?";
        String insertSql = "INSERT INTO carts(user_id) VALUES (?)";

        try (Connection con = DBUtil.getConnection()) {

            // ✅ Check if cart exists
            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setInt(1, userId);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    return rs.getInt("cart_id");
                }
            }

            // ✅ Create new cart
            try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                insertPs.setInt(1, userId);
                insertPs.executeUpdate();
            }

            // ✅ Fetch newly created cart
            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setInt(1, userId);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    return rs.getInt("cart_id");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /* ================= ADD TO CART ================= */
    public void addToCart(int cartId, int productId, int quantity) {

        String checkSql = "SELECT quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO cart_items(cart_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE cart_items SET quantity = quantity + ? WHERE cart_id = ? AND product_id = ?";

        try (Connection con = DBUtil.getConnection()) {

            // ✅ Check if product already exists in cart
            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setInt(1, cartId);
                checkPs.setInt(2, productId);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    // ✅ Update quantity
                    try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                        updatePs.setInt(1, quantity);
                        updatePs.setInt(2, cartId);
                        updatePs.setInt(3, productId);
                        updatePs.executeUpdate();
                    }

                    System.out.println("✅ Cart updated (quantity increased)");
                } else {
                    // ✅ Insert new item
                    try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                        insertPs.setInt(1, cartId);
                        insertPs.setInt(2, productId);
                        insertPs.setInt(3, quantity);
                        insertPs.executeUpdate();
                    }

                    System.out.println("✅ Product added to cart");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= FETCH CART ITEMS BY USER ================= */
    public List<CartItem> getCartItemsByUser(int userId) {

        List<CartItem> items = new ArrayList<>();

        String sql = """
            SELECT ci.product_id, ci.quantity, p.price
            FROM cart_items ci
            JOIN carts c ON ci.cart_id = c.cart_id
            JOIN products p ON ci.product_id = p.product_id
            WHERE c.user_id = ?
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));

                items.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    /* ================= CLEAR CART BY USER ================= */
    public void clearCartByUser(int userId) {

        String sql = """
            DELETE FROM cart_items
            WHERE cart_id = (SELECT cart_id FROM carts WHERE user_id = ?)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= REMOVE FROM CART ================= */
    public void removeFromCart(int cartId, int productId, int quantity) {

        String checkSql = "SELECT quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
        String updateSql = "UPDATE cart_items SET quantity = quantity - ? WHERE cart_id = ? AND product_id = ?";
        String deleteSql = "DELETE FROM cart_items WHERE cart_id = ? AND product_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {

            checkPs.setInt(1, cartId);
            checkPs.setInt(2, productId);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {

                int existingQty = rs.getInt("quantity");

                if (quantity >= existingQty) {
                    try (PreparedStatement deletePs = con.prepareStatement(deleteSql)) {
                        deletePs.setInt(1, cartId);
                        deletePs.setInt(2, productId);
                        deletePs.executeUpdate();
                    }

                    System.out.println("✅ Item removed completely");

                } else {
                    try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                        updatePs.setInt(1, quantity);
                        updatePs.setInt(2, cartId);
                        updatePs.setInt(3, productId);
                        updatePs.executeUpdate();
                    }

                    System.out.println("✅ Cart quantity reduced");
                }

            } else {
                System.out.println("❌ Item not found in cart");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= GET CART ID BY USER ================= */
    public int getCartIdByUser(int userId) {

        String sql = "SELECT cart_id FROM carts WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("cart_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}