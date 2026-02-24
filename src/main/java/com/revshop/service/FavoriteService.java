package com.revshop.service;

import com.revshop.dao.FavoriteDao;
import com.revshop.model.Favorite;

import java.util.List;

public class FavoriteService {

    private final FavoriteDao favoriteDao = new FavoriteDao();

    public void toggleFavorite(int userId, int productId) {
        favoriteDao.toggleFavorite(userId, productId);
    }

    public void addFavorite(int userId, int productId) {
        favoriteDao.addFavorite(userId, productId);
    }

    public void removeFavorite(int userId, int productId) {
        favoriteDao.removeFavorite(userId, productId);
    }

    public void viewFavorites(int userId) {

        List<Favorite> list = favoriteDao.getFavoritesByUser(userId);

        if (list.isEmpty()) {
            System.out.println("💔 No favorite products");
            return;
        }

        System.out.println("\n===== YOUR FAVORITES =====");

        for (Favorite fav : list) {
            System.out.println("❤️ " + fav.getProductName());
            System.out.println("💰 ₹" + fav.getPrice());
            System.out.println("--------------------------");
        }
    }
}