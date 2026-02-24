package com.revshop.dao;

import com.revshop.model.Product;
import com.revshop.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SellerDao {

    // ✅ Create seller profile
    public void createSeller(int userId, String businessName) {

        String sql = "INSERT INTO sellers(user_id, business_name) VALUES (?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, businessName);

            ps.executeUpdate();
            System.out.println("✅ Seller registered successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Fetch seller_id using user_id
    public int getSellerIdByUserId(int userId) {

        String sql = "SELECT seller_id FROM SYSTEM.sellers WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("seller_id");
                System.out.println("DEBUG → Seller FOUND → seller_id = " + id);
                return id;
            } else {
                System.out.println("DEBUG → Query returned ZERO rows");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ✅ View seller products
    public List<Product> getSellerProducts(int sellerId) {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products WHERE seller_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setCategory(rs.getString("category"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));

                products.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}
