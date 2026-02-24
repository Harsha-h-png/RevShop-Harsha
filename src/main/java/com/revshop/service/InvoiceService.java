package com.revshop.service;

import com.revshop.dao.OrderDao;
import com.revshop.model.Order;
import com.revshop.model.OrderItem;

import java.util.List;

public class InvoiceService {

    private final OrderDao orderDao = new OrderDao();

    public void generateInvoice(int orderId) {

        Order order = orderDao.getOrderDetails(orderId);

        if (order == null) {
            System.out.println("❌ Invoice generation failed. Order not found.");
            return;
        }

        System.out.println("\n========= INVOICE =========");
        System.out.println("Order ID : " + order.getOrderId());
        System.out.println("Date     : " + order.getFormattedDate());
        System.out.println("Status   : " + order.getStatus());
        System.out.println("--------------------------------");

        System.out.printf("%-20s %-5s %-10s%n", "Product", "Qty", "Price");
        System.out.println("--------------------------------");

        List<OrderItem> items = order.getItems();

        for (OrderItem item : items) {
            System.out.printf("%-20s %-5d ₹%-10.2f%n",
                    item.getProductName(),
                    item.getQuantity(),
                    item.getPrice());
        }

        System.out.println("--------------------------------");
        System.out.println("Total Amount : ₹" + order.getTotalAmount());
        System.out.println("================================");
    }
}