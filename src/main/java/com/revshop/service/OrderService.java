package com.revshop.service;

import com.revshop.dao.CartDao;
import com.revshop.dao.OrderDao;
import com.revshop.dao.ProductDao;
import com.revshop.model.CartItem;
import com.revshop.model.Order;
import com.revshop.model.OrderItem;

import java.util.List;
import java.util.Scanner;

public class OrderService {

    private final CartDao cartDao = new CartDao();
    private final OrderDao orderDao = new OrderDao();
    private final ProductDao productDao = new ProductDao();
    private final PaymentService paymentService = new PaymentService();
    private final NotificationService notificationService = new NotificationService();
    private final InvoiceService invoiceService = new InvoiceService();

    //  Checkout
    public void checkout(int userId) {

        List<CartItem> items = cartDao.getCartItemsByUser(userId);

        if (items.isEmpty()) {
            System.out.println("🛒 Cart is empty. Cannot checkout.");
            return;
        }

        double totalAmount = 0;

        for (CartItem item : items) {
            totalAmount += item.getPrice() * item.getQuantity();
        }

        Scanner sc = new Scanner(System.in);

        System.out.print("Proceed to checkout? (yes/no): ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("❌ Checkout cancelled");
            return;
        }

        System.out.println("\nSelect Payment Method:");
        System.out.println("1. UPI");
        System.out.println("2. Card");
        System.out.println("3. Cash on Delivery");
        System.out.print("Enter choice: ");

        int choice = Integer.parseInt(sc.nextLine());

        String method;

        switch (choice) {
            case 1 -> method = "UPI";
            case 2 -> method = "CARD";
            case 3 -> method = "COD";
            default -> {
                System.out.println("❌ Invalid payment option");
                return;
            }
        }

        //  Create Order
        int orderId = orderDao.createOrder(userId, totalAmount);

        if (orderId == -1) {
            System.out.println("❌ Failed to place order");
            return;
        }

        //  Insert Items + Reduce Stock
        for (CartItem item : items) {

            orderDao.addOrderItem(
                    orderId,
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice()
            );

            int currentStock = productDao.getStock(item.getProductId());

            if (currentStock < item.getQuantity()) {
                System.out.println("❌ Not enough stock for product: " + item.getProductId());
                return;
            }

            int newStock = currentStock - item.getQuantity();
            productDao.updateStockValue(item.getProductId(), newStock);
        }

        //  Payment
        paymentService.processPayment(orderId, totalAmount, method);

        //  Clear Cart
        cartDao.clearCartByUser(userId);

        System.out.println("✅ Order placed successfully!");
        invoiceService.generateInvoice(orderId);
        notificationService.notify(userId,
                "✅ Order placed successfully! Total ₹" + totalAmount);
        System.out.println("💰 Total Paid: ₹" + totalAmount);
    }

    //  View Orders
    public void viewOrders(int userId) {

        List<Order> orders = orderDao.getOrdersByUser(userId);

        if (orders.isEmpty()) {
            System.out.println("📭 No orders found");
            return;
        }

        System.out.println("\n===== YOUR ORDERS =====");

        for (Order order : orders) {

            List<OrderItem> items = orderDao.getOrderItems(order.getOrderId());
            order.setItems(items);

            System.out.println("Order ID : " + order.getOrderId());
            System.out.println("Total    : ₹" + order.getTotalAmount());
            System.out.println("Status   : " + order.getStatus());
            System.out.println("Date     : " + order.getFormattedDate());

            System.out.println("Items:");

            for (OrderItem item : items) {
                System.out.println(" - " + item.getProductName()
                        + " | Qty: " + item.getQuantity()
                        + " | ₹" + item.getPrice());
            }

            System.out.println("--------------------------");
        }
    }

    //  Cancel Order (FIXED)
    public void cancelOrder(int orderId) {

        Order order = orderDao.getOrderById(orderId);

        //  Validate existence
        if (order == null) {
            System.out.println("❌ Order not found");
            return;
        }

        //  Validate status
        String status = order.getStatus();

        if (status.equalsIgnoreCase("CANCELLED")) {
            System.out.println("❌ Order already cancelled");
            return;
        }

        if (status.equalsIgnoreCase("DELIVERED")) {
            System.out.println("❌ Delivered orders cannot be cancelled");
            return;
        }

        double refundAmount = order.getTotalAmount();

        //  Cancel in DB
        boolean cancelled = orderDao.cancelOrder(orderId);

        if (!cancelled) {
            System.out.println("❌ Failed to cancel order");
            return;
        }

        //  Refund only if cancel success
        paymentService.refundPayment(orderId, refundAmount);

        System.out.println("💰 Refund Initiated: ₹" + refundAmount);
    }
}