# 🛒 RevShop – Console-Based E-Commerce Application

RevShop is a **Java console-based e-commerce system** built using **JDBC and Oracle Database (XE)**.  
The application supports **Buyer** and **Seller** roles with features like product management, cart operations, order processing, payments, and reviews.

---

## 🚀 Features

### 👤 Authentication
- User Registration
- User Login
- Role-based access (BUYER / SELLER)

---

### 🛍 Buyer Module
- View Products
- Add to Cart
- Remove Item from Cart
- View Cart with Total Price
- Checkout
- Payment (UPI / Card / Cash on Delivery)
- View Orders
- Cancel Orders
- Refund on Cancellation
- Add Reviews
- View Favorites
- Notifications

---

### 🏪 Seller Module
- Add Products
- View My Products
- Update Stock (adds to existing stock)

---

### 📦 Order Management
- Create Order
- Add Order Items
- Order Status (PLACED / CANCELLED)
- Order Date
- Stock Update on Checkout
- Stock Restore on Cancellation

---

### 💳 Payment Module
- Record Payment
- Refund Handling

---

## 🧰 Tech Stack

| Layer        | Technology |
|-------------|------------|
| Language     | Java (JDK 21) |
| Database     | Oracle XE (18c) |
| Connectivity | JDBC |
| IDE          | IntelliJ IDEA |
| Build Tool   | Maven |
| Driver       | ojdbc11 |

---

## 🗄 Database Schema (Simplified)

**Tables Used:**
- USERS
- SELLERS
- PRODUCTS
- CARTS
- CART_ITEMS
- ORDERS
- ORDER_ITEMS
- PAYMENTS
- REVIEWS
- FAVORITES
- NOTIFICATIONS

---

## ⚙️ Setup Instructions

### ✅ 1. Prerequisites
- Java JDK 21+
- Oracle XE installed and running
- Maven installed
- ojdbc11 dependency added

---

### ✅ 2. Database Connection

Update `DBUtil.java`:

```java
private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
private static final String USERNAME = "system";
private static final String PASSWORD = "admin";
