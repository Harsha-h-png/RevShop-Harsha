package com.revshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.revshop.util.DBUtil;

public class PasswordResetDao {

    public void updatePassword(String email, String newPassword) {

        String sql = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
