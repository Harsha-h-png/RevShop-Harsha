package com.revshop.controller;

import com.revshop.model.User;
import com.revshop.service.FavoriteService;

public class FavoriteController {

    private final FavoriteService favoriteService = new FavoriteService();

    public void viewFavorites(User user) {

        if (user == null) {
            System.out.println("⚠ Please login first");
            return;
        }

        favoriteService.viewFavorites(user.getUserId());
    }

    public void toggleFavorite(User user, int productId) {

        if (user == null) {
            System.out.println("⚠ Please login first");
            return;
        }

        favoriteService.toggleFavorite(user.getUserId(), productId);
    }

    //  REMOVE FAVORITE
    public void removeFavorite(User user, int productId) {

        if (user == null) {
            System.out.println("⚠ Please login first");
            return;
        }

        favoriteService.removeFavorite(user.getUserId(), productId);
    }
}