package com.revshop.controller;

import com.revshop.model.User;
import com.revshop.service.OrderService;

import java.util.Scanner;

public class OrderController {

    private final OrderService orderService = new OrderService();
    private final Scanner sc = new Scanner(System.in);   // ✅ FIXED

    // ✅ Checkout
    public void checkout(User user) {

        if (user == null) {
            System.out.println("❌ Please login first");
            return;
        }

        orderService.checkout(user.getUserId());
    }

    // ✅ View Orders
    public void viewOrders(User user) {

        if (user == null) {
            System.out.println("❌ Please login first");
            return;
        }

        orderService.viewOrders(user.getUserId());
    }

    // ✅ Cancel Order
    public void cancelOrder(User user) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Order ID to cancel: ");
        int orderId = Integer.parseInt(sc.nextLine());

        System.out.print("Are you sure? (Y/N): ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("❌ Cancellation aborted");
            return;
        }

        orderService.cancelOrder(orderId);
    }
}
