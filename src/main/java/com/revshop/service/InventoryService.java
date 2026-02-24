package com.revshop.service;

import com.revshop.dao.ProductDao;

public class InventoryService {

    private final ProductDao productDao = new ProductDao();

    public void updateProductStock(int productId, int quantity) {
        productDao.updateStock(productId, quantity);
    }
}
