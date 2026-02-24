package com.revshop.main;

import com.revshop.controller.*;
import com.revshop.model.User;
import com.revshop.service.InvoiceService;

import java.util.Scanner;

public class RevShopApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AuthController authController = new AuthController();
        BuyerController buyerController = new BuyerController();
        CartController cartController = new CartController();
        FavoriteController favoriteController = new FavoriteController();
        OrderController orderController = new OrderController();
        NotificationController notificationController = new NotificationController();
        ReviewController reviewController = new ReviewController();
        SellerController sellerController = new SellerController();
        InvoiceService invoiceService = new InvoiceService();

        User loggedInUser = null;

        while (true) {

            /* ================= NOT LOGGED IN ================= */
            if (loggedInUser == null) {

                System.out.println("\n==== REVSHOP MENU ====");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");

                String input = sc.nextLine();

                if (input.isBlank()) {
                    System.out.println("❌ Choice cannot be empty");
                    continue;
                }

                int choice;

                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1 -> authController.register();
                    case 2 -> loggedInUser = authController.login();
                    case 3 -> {
                        System.out.println("👋 Thank you for using RevShop!");
                        System.exit(0);
                    }
                    default -> System.out.println("❌ Invalid option");
                }
            }

            /* ================= BUYER DASHBOARD ================= */
            else if ("BUYER".equalsIgnoreCase(loggedInUser.getRole())) {

                System.out.println("\n==== BUYER DASHBOARD ====");
                System.out.println("1. View Products");
                System.out.println("2. View Cart");
                System.out.println("3. Add to Cart");
                System.out.println("4. Remove Item from Cart");
                System.out.println("5. View Favorites");
                System.out.println("6. Checkout");
                System.out.println("7. View Orders");
                System.out.println("8. Cancel Order");
                System.out.println("9. Notifications");
                System.out.println("10. Add Review");
                System.out.println("11. Toggle Favorite ❤️");
                System.out.println("12. Remove Favorite 💔");
                System.out.println("13. View Invoice 🧾");
                System.out.println("14. Logout");

                System.out.print("Enter choice: ");
                String input = sc.nextLine();

                if (input.isBlank()) {
                    System.out.println("❌ Choice cannot be empty");
                    continue;
                }

                int ch;

                try {
                    ch = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input. Please enter a number.");
                    continue;
                }

                switch (ch) {

                    case 1 -> buyerController.viewAllProducts(loggedInUser);

                    case 2 -> cartController.viewCart(loggedInUser);

                    case 3 -> buyerController.addToCart(loggedInUser);

                    case 4 -> cartController.removeItem(loggedInUser);

                    case 5 -> favoriteController.viewFavorites(loggedInUser);

                    case 6 -> orderController.checkout(loggedInUser);

                    case 7 -> orderController.viewOrders(loggedInUser);

                    case 8 -> orderController.cancelOrder(loggedInUser);

                    case 9 -> notificationController.showNotifications(loggedInUser);

                    case 10 -> reviewController.addReview(loggedInUser);

                    // ✅ Toggle Favorite ❤️
                    case 11 -> {
                        System.out.print("Enter Product ID: ");
                        String pidInput = sc.nextLine();

                        try {
                            int productId = Integer.parseInt(pidInput);
                            favoriteController.toggleFavorite(loggedInUser, productId);
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Invalid product ID");
                        }
                    }

                    // ✅ Remove Favorite 💔
                    case 12 -> {
                        System.out.print("Enter Product ID to remove: ");
                        String pidInput = sc.nextLine();

                        try {
                            int productId = Integer.parseInt(pidInput);
                            favoriteController.removeFavorite(loggedInUser, productId);
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Invalid product ID");
                        }
                    }

                    // ✅ View Invoice 🧾
                    case 13 -> {
                        System.out.print("Enter Order ID: ");
                        String orderInput = sc.nextLine();

                        try {
                            int orderId = Integer.parseInt(orderInput);
                            invoiceService.generateInvoice(orderId);
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Invalid order ID");
                        }
                    }

                    // ✅ Logout
                    case 14 -> {
                        loggedInUser = null;
                        System.out.println("🔓 Logged out successfully");
                    }

                    default -> System.out.println("❌ Invalid option");
                }
            }

            /* ================= SELLER DASHBOARD ================= */
            else if ("SELLER".equalsIgnoreCase(loggedInUser.getRole())) {

                System.out.println("\n==== SELLER DASHBOARD ====");
                System.out.println("1. Add Product");
                System.out.println("2. View My Products");
                System.out.println("3. Update Stock");
                System.out.println("4. Logout");
                System.out.print("Enter choice: ");

                String input = sc.nextLine();

                if (input.isBlank()) {
                    System.out.println("❌ Choice cannot be empty");
                    continue;
                }

                int ch;

                try {
                    ch = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input. Please enter a number.");
                    continue;
                }

                switch (ch) {

                    case 1 -> sellerController.addProduct(loggedInUser);

                    case 2 -> sellerController.viewProducts(loggedInUser);

                    case 3 -> sellerController.updateStock(loggedInUser);

                    case 4 -> {
                        loggedInUser = null;
                        System.out.println("🔓 Logged out successfully");
                    }

                    default -> System.out.println("❌ Invalid option");
                }
            }
        }
    }
}