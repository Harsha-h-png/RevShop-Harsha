package com.revshop.service;

import com.revshop.dao.SellerDao;
import com.revshop.model.Product;

import java.util.List;

public class SellerService {

    private final SellerDao sellerDao = new SellerDao();

    public void registerSeller(int userId, String businessName) {
        sellerDao.createSeller(userId, businessName);
    }

    public void viewSellerProducts(int sellerId, String sellerName) {

        List<Product> products = sellerDao.getSellerProducts(sellerId);

        System.out.println("\n===== " + sellerName.toUpperCase() + " STORE =====");

        if (products.isEmpty()) {
            System.out.println("📦 No products in your store");
            return;
        }

        for (Product p : products) {
            System.out.println("ID       : " + p.getProductId());
            System.out.println("Name     : " + p.getName());
            System.out.println("Category : " + p.getCategory());
            System.out.println("Price    : ₹" + p.getPrice());
            System.out.println("Stock    : " + p.getStock());
            System.out.println("----------------------------");
        }
    }
}