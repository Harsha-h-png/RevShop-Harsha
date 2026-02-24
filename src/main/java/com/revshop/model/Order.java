package com.revshop.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class Order {

    private int orderId;
    private int userId;
    private double totalAmount;
    private String status;
    private Timestamp orderDate;

    //  REQUIRED for product display
    private List<OrderItem> items;

    // ===== GETTERS & SETTERS =====

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    // ✅ DATE FORMATTER
    public String getFormattedDate() {

        if (orderDate == null) return "N/A";

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
        return sdf.format(orderDate);
    }
}