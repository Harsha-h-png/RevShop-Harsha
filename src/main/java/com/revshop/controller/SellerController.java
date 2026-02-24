package com.revshop.controller;

import com.revshop.dao.SellerDao;
import com.revshop.model.Product;
import com.revshop.model.User;
import com.revshop.service.ProductService;
import com.revshop.service.SellerService;

import java.util.Scanner;

public class SellerController {

    private final ProductService productService = new ProductService();
    private final SellerDao sellerDao = new SellerDao();
    private final SellerService sellerService = new SellerService();
    private final Scanner sc = new Scanner(System.in);

    /* ================= ADD PRODUCT ================= */
    public void addProduct(User seller) {

        if (seller == null) {
            System.out.println("❌ Please login first");
            return;
        }

        int sellerId = sellerDao.getSellerIdByUserId(seller.getUserId());

        // ✅ AUTO-CREATE SELLER PROFILE (BEST UX)
        if (sellerId == -1) {

            System.out.println("\n🆕 Welcome new seller!");
            System.out.print("Enter Business Name: ");
            String businessName = sc.nextLine();

            if (businessName.isBlank()) {
                System.out.println("❌ Business name cannot be empty");
                return;
            }

            sellerService.registerSeller(seller.getUserId(), businessName);

            sellerId = sellerDao.getSellerIdByUserId(seller.getUserId());

            if (sellerId == -1) {
                System.out.println("❌ Failed to create seller profile");
                return;
            }
        }

        Product product = new Product();
        product.setSellerId(sellerId);

        System.out.print("Enter Product Name: ");
        String name = sc.nextLine();

        if (name.isBlank()) {
            System.out.println("❌ Product name cannot be empty");
            return;
        }
        product.setName(name);

        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        if (category.isBlank()) {
            System.out.println("❌ Category cannot be empty");
            return;
        }
        product.setCategory(category);

        try {

            System.out.print("Enter Price: ");
            String priceInput = sc.nextLine();

            if (priceInput.isBlank()) {
                System.out.println("❌ Price cannot be empty");
                return;
            }

            double price = Double.parseDouble(priceInput);

            if (price <= 0) {
                System.out.println("❌ Price must be greater than zero");
                return;
            }
            product.setPrice(price);

            System.out.print("Enter Stock: ");
            String stockInput = sc.nextLine();

            if (stockInput.isBlank()) {
                System.out.println("❌ Stock cannot be empty");
                return;
            }

            int stock = Integer.parseInt(stockInput);

            if (stock < 0) {
                System.out.println("❌ Stock cannot be negative");
                return;
            }
            product.setStock(stock);

            // ✅ NO NEED manual Product ID (sequence handles it)
            productService.addProduct(product);

            System.out.println("✅ Product added to your store");

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid numeric value entered");
        }
    }

    /* ================= VIEW PRODUCTS ================= */
    public void viewProducts(User seller) {

        if (seller == null) {
            System.out.println("❌ Please login first");
            return;
        }

        int sellerId = sellerDao.getSellerIdByUserId(seller.getUserId());

        if (sellerId == -1) {
            System.out.println("📦 No seller profile found");
            System.out.println("👉 Add a product to auto-create profile");
            return;
        }

        sellerService.viewSellerProducts(sellerId, seller.getName());
    }

    /* ================= UPDATE STOCK ================= */
    public void updateStock(User seller) {

        if (seller == null) {
            System.out.println("❌ Please login first");
            return;
        }

        int sellerId = sellerDao.getSellerIdByUserId(seller.getUserId());

        if (sellerId == -1) {
            System.out.println("❌ Seller profile not found");
            return;
        }

        try {

            System.out.print("Enter Product ID: ");
            String pidInput = sc.nextLine();

            if (pidInput.isBlank()) {
                System.out.println("❌ Product ID cannot be empty");
                return;
            }

            int productId = Integer.parseInt(pidInput);

            System.out.print("Enter Quantity to Add: ");
            String qtyInput = sc.nextLine();

            if (qtyInput.isBlank()) {
                System.out.println("❌ Quantity cannot be empty");
                return;
            }

            int qtyToAdd = Integer.parseInt(qtyInput);

            if (qtyToAdd <= 0) {
                System.out.println("❌ Quantity must be positive");
                return;
            }

            productService.addStock(productId, qtyToAdd);

            System.out.println("✅ Stock updated successfully");

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid numeric value entered");
        }
    }
}