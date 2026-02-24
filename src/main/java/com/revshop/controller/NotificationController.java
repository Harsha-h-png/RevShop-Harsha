package com.revshop.controller;

import com.revshop.model.User;
import com.revshop.service.NotificationService;

public class NotificationController {

    private final NotificationService notificationService = new NotificationService();

    public void showNotifications(User user) {

        if (user == null) {
            System.out.println("⚠ Please login first");
            return;
        }

        notificationService.viewNotifications(user.getUserId());
    }
}