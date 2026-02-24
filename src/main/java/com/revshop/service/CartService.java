package com.revshop.service;

import com.revshop.dao.CartDao;
import com.revshop.model.CartItem;

import java.util.List;

public class CartService {

    private final CartDao cartDao = new CartDao();

    // ✅ Add product to cart
    public void addProductToCart(int userId, int productId, int quantity) {

        int cartId = cartDao.getOrCreateCart(userId);

        if (cartId == -1) {
            System.out.println("❌ Unable to create cart");
            return;
        }

        cartDao.addToCart(cartId, productId, quantity);
    }

    // ✅ Fetch cart items
    public List<CartItem> getCartItems(int userId) {
        return cartDao.getCartItemsByUser(userId);
    }

    // ✅ Remove product
    public void removeProduct(int userId, int productId, int quantity) {

        int cartId = cartDao.getOrCreateCart(userId);
        cartDao.removeFromCart(cartId, productId, quantity);
    }


    // ✅ Calculate total
    public double calculateTotal(List<CartItem> items) {

        double total = 0;

        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }

        return total;
    }
}
