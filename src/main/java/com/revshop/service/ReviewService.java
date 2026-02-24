package com.revshop.service;

import com.revshop.dao.ReviewDao;

public class ReviewService {

    private ReviewDao reviewDao = new ReviewDao();

    public void addReview(int productId, String review) {
        reviewDao.saveReview(productId, review);
    }

    public void viewReviews(int productId) {
        reviewDao.getReviews(productId);
    }
}
