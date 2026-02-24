package com.revshop.dao;

import com.revshop.model.Notification;
import com.revshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {

    public void addNotification(int userId, String message) {

        String sql = """
            INSERT INTO notifications (
                notification_id, user_id, message, created_at, is_read
            )
            VALUES (notifications_seq.NEXTVAL, ?, ?, SYSTIMESTAMP, 'NO')
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getUserNotifications(int userId) {

        List<Notification> list = new ArrayList<>();

        String sql = """
            SELECT notification_id, message, created_at, is_read
            FROM notifications
            WHERE user_id = ?
            ORDER BY created_at DESC
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Notification n = new Notification();
                n.setNotificationId(rs.getInt("notification_id"));
                n.setMessage(rs.getString("message"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                n.setIsRead(rs.getString("is_read"));

                list.add(n);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void markAllRead(int userId) {

        String sql = "UPDATE notifications SET is_read = 'YES' WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}