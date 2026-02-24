package com.revshop.dao;

import com.revshop.model.Favorite;
import com.revshop.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

public class FavoriteDao {

    // ✅ CHECK if already favorite
    public boolean isFavorite(int userId, int productId) {

        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND product_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ✅ ADD (Duplicate-safe)
    public void addFavorite(int userId, int productId) {

        if (isFavorite(userId, productId)) {
            System.out.println("⚠ Product already in favorites");
            return;
        }

        String sql = """
            INSERT INTO favorites (favorite_id, user_id, product_id)
            VALUES (favorites_seq.NEXTVAL, ?, ?)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            ps.executeUpdate();
            System.out.println("❤️ Added to favorites");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ REMOVE
    public void removeFavorite(int userId, int productId) {

        String sql = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("💔 Removed from favorites");
            else
                System.out.println("⚠ Product not in favorites");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ TOGGLE FAVORITE ⭐
    public void toggleFavorite(int userId, int productId) {

        if (isFavorite(userId, productId)) {
            removeFavorite(userId, productId);
        } else {
            addFavorite(userId, productId);
        }
    }

    public List<Favorite> getFavoritesByUser(int userId) {

        List<Favorite> list = new ArrayList<>();

        String sql = """
        SELECT f.favorite_id,
               p.product_id,
               p.name,
               p.price
        FROM favorites f
        JOIN products p ON f.product_id = p.product_id
        WHERE f.user_id = ?
    """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Favorite fav = new Favorite();
                fav.setFavoriteId(rs.getInt("favorite_id"));
                fav.setProductId(rs.getInt("product_id"));
                fav.setProductName(rs.getString("name"));
                fav.setPrice(rs.getDouble("price"));

                list.add(fav);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}