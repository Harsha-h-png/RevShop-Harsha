package com.revshop.service;

import com.revshop.dao.NotificationDao;
import com.revshop.model.Notification;

import java.util.List;

public class NotificationService {

    private final NotificationDao notificationDao = new NotificationDao();

    public void notify(int userId, String message) {
        notificationDao.addNotification(userId, message);
    }

    public void viewNotifications(int userId) {

        List<Notification> list = notificationDao.getUserNotifications(userId);

        if (list.isEmpty()) {
            System.out.println("🔔 No notifications");
            return;
        }

        System.out.println("\n===== NOTIFICATIONS =====");

        for (Notification n : list) {
            System.out.println("📢 " + n.getMessage());
            System.out.println("🕒 " + n.getCreatedAt());
            System.out.println("--------------------------");
        }

        notificationDao.markAllRead(userId);
    }
}