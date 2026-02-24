package com.revshop.controller;

import com.revshop.model.Product;
import com.revshop.model.User;
import com.revshop.service.BuyerService;
import com.revshop.service.CartService;
import com.revshop.service.ProductService;

import java.util.List;
import java.util.Scanner;

public class BuyerController {

    private final Scanner sc = new Scanner(System.in);
    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();
    private final BuyerService buyerService = new BuyerService();

    /* ================= VIEW PRODUCTS ================= */
    public void viewAllProducts(User user) {

        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("📭 No products available");
            return;
        }

        System.out.println("\n==== AVAILABLE PRODUCTS ====");
        System.out.printf("%-5s %-20s %-12s %-10s%n", "ID", "Name", "Price", "Stock");
        System.out.println("----------------------------------------------------");

        for (Product p : products) {
            System.out.printf("%-5d %-20s ₹%-10.2f %-10d%n",
                    p.getProductId(),
                    p.getName(),
                    p.getPrice(),
                    p.getStock());
        }
    }

    /* ================= ADD TO CART ================= */
    public void addToCart(User user) {

        if (user == null) {
            System.out.println("❌ Please login first");
            return;
        }

        System.out.print("Enter Product ID: ");
        String pidInput = sc.nextLine();

        if (pidInput.isBlank()) {
            System.out.println("❌ Product ID cannot be empty");
            return;
        }

        System.out.print("Enter Quantity: ");
        String qtyInput = sc.nextLine();

        if (qtyInput.isBlank()) {
            System.out.println("❌ Quantity cannot be empty");
            return;
        }

        try {
            int productId = Integer.parseInt(pidInput);
            int quantity = Integer.parseInt(qtyInput);

            if (quantity <= 0) {
                System.out.println("❌ Quantity must be greater than zero");
                return;
            }

            int stock = productService.getStock(productId);

            if (stock == 0) {
                System.out.println("❌ Product out of stock");
                return;
            }

            if (quantity > stock) {
                System.out.println("❌ Only " + stock + " items available");
                return;
            }

            buyerService.addToCart(user.getUserId(), productId, quantity);

            System.out.println("✅ Product added to cart");

        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid numeric value");
        }
    }
}