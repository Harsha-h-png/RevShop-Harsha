package com.revshop.service;

import com.revshop.dao.CartDao;

public class BuyerService {

    private final CartDao cartDao = new CartDao();

    public void addToCart(int userId, int productId, int quantity) {

        int cartId = cartDao.getOrCreateCart(userId);

        if (cartId == -1) {
            System.out.println("❌ Failed to get/create cart");
            return;
        }

        System.out.println("DEBUG → cartId = " + cartId);  // 👈 IMPORTANT

        cartDao.addToCart(cartId, productId, quantity);
    }
}