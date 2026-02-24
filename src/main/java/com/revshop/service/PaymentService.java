package com.revshop.service;

import com.revshop.dao.PaymentDao;

public class PaymentService {

    private final PaymentDao paymentDao = new PaymentDao();

    public void processPayment(int orderId, double amount, String method) {

        boolean paid = paymentDao.createPayment(orderId, amount, method);

        if (paid)
            System.out.println("✅ Payment successful");
        else
            System.out.println("❌ Payment failed");
    }

    public void refundPayment(int orderId, double amount) {

        boolean refunded = paymentDao.createRefund(orderId, amount);

        if (refunded)
            System.out.println("✅ Refund processed successfully");
        else
            System.out.println("❌ Refund failed");
    }
}