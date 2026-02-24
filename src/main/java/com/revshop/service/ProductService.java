package com.revshop.service;

import com.revshop.dao.ProductDao;
import com.revshop.model.Product;

import java.util.List;

public class ProductService {

    private final ProductDao productDao = new ProductDao();

    // ✅ Add Product
    public void addProduct(Product product) {
        productDao.addProduct(product);
    }

    // ✅ View products by seller
    public void viewProductsBySeller(int sellerId, String sellerName) {

        List<Product> products = productDao.getProductsBySeller(sellerId);

        System.out.println("\n===== " + sellerName.toUpperCase() + " STORE =====");

        if (products.isEmpty()) {
            System.out.println("📦 No products in your store");
            return;
        }

        for (Product p : products) {
            System.out.println(
                    p.getProductId() + " | " +
                            p.getName() + " | " +
                            p.getCategory() + " | ₹" +
                            p.getPrice() + " | Stock: " +
                            p.getStock()
            );
        }
    }

    // ✅ Buyer → All products
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public void updateStock(int productId, int newStock) {
        productDao.updateStock(productId, newStock);
    }

    public void addStock(int productId, int qtyToAdd) {
        productDao.updateStock(productId, qtyToAdd);
    }

    public int getStock(int productId) {
        return productDao.getStock(productId);
    }
}
