package com.revshop.dao;

import java.sql.*;

import com.revshop.util.DBUtil;

public class BuyerDao {

    public boolean buyerExists(int buyerId) {

        String sql = "SELECT * FROM users WHERE user_id=? AND UPPER(role)='BUYER'";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
