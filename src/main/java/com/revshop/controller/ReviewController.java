package com.revshop.controller;

import java.util.Scanner;
import com.revshop.model.User;
import com.revshop.service.ReviewService;

public class ReviewController {

    private ReviewService reviewService = new ReviewService();
    private Scanner sc = new Scanner(System.in);

    public void addReview(User user) {
        if (user == null) {
            System.out.println("⚠ Please login first");
            return;
        }

        System.out.print("Enter Product ID: ");
        int productId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Review: ");
        String review = sc.nextLine();

        reviewService.addReview(productId, review);
    }
}
