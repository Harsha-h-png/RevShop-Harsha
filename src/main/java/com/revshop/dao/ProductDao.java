package com.revshop.dao;

import com.revshop.model.Product;
import com.revshop.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    //  Add Product
    public void addProduct(Product product) {

        String sql = """
        INSERT INTO products(product_id, seller_id, name, category, price, stock)
        VALUES (products_seq.NEXTVAL, ?, ?, ?, ?, ?)
    """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, product.getSellerId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getCategory());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getStock());

            ps.executeUpdate();

            System.out.println("✅ Product added successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  Get products by seller
    public List<Product> getProductsBySeller(int sellerId) {

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE seller_id = ?";

        Connection con = DBUtil.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {


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

    //  Get ALL products (Buyer)
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        Connection con = DBUtil.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {


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

    // Update product stock (FIXED POSITION)
    public void updateStock(int productId, int addedQuantity) {

        String sql = """
        UPDATE products
        SET stock = stock + ?
        WHERE product_id = ?
    """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, addedQuantity);
            ps.setInt(2, productId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Stock increased successfully");
            } else {
                System.out.println("❌ Product not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//UPDATE STOCK VALUE//
    public void updateStockValue(int productId, int stock) {

        String sql = "UPDATE products SET stock = ? WHERE product_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, stock);
            ps.setInt(2, productId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Stock updated successfully");
            } else {
                System.out.println("❌ Product not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getStock(int productId) {

        String sql = "SELECT stock FROM products WHERE product_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("stock");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void increaseStock(int productId, int quantity) {

        String sql = "UPDATE products SET stock = stock + ? WHERE product_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, productId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
