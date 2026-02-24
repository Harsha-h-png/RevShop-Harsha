package com.revshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revshop.util.DBUtil;

public class ReviewDao {

    // SAVE REVIEW
    public void saveReview(int productId, String review) {

        String sql = "INSERT INTO reviews (product_id, review) VALUES (?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setString(2, review);
            ps.executeUpdate();

            System.out.println("Review added successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW REVIEWS
    public void getReviews(int productId) {

        String sql = "SELECT review FROM reviews WHERE product_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            System.out.println("---- Reviews ----");
            while (rs.next()) {
                System.out.println(rs.getString("review"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
