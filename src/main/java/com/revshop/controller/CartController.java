package com.revshop.controller;

import com.revshop.model.CartItem;
import com.revshop.model.User;
import com.revshop.service.CartService;

import java.util.List;
import java.util.Scanner;

public class CartController {

    private final CartService cartService = new CartService();
    private final Scanner sc = new Scanner(System.in);

    // ✅ View Cart
    public void viewCart(User user) {

        List<CartItem> items = cartService.getCartItems(user.getUserId());

        if (items.isEmpty()) {
            System.out.println("🛒 Cart is empty");
            return;
        }

        System.out.println("\n===== YOUR CART =====");

        for (CartItem item : items) {

            double subtotal = item.getPrice() * item.getQuantity();

            System.out.println(
                    item.getProductId() +
                            " | Qty: " + item.getQuantity() +
                            " | ₹" + item.getPrice() +
                            " | Subtotal: ₹" + subtotal
            );

            System.out.println("----------------------------");
        }

        double total = cartService.calculateTotal(items);
        System.out.println("💰 TOTAL PRICE : ₹" + total);
    }

    // ✅ Remove Item
    public void removeItem(User user) {

        System.out.print("Enter Product ID to remove: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("Enter Quantity to remove: ");
        int qty = Integer.parseInt(sc.nextLine());

        cartService.removeProduct(user.getUserId(), pid, qty);
    }

}