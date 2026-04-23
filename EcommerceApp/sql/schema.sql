-- =====================================================================
-- Wingify Beauty eCommerce — MySQL Schema
-- =====================================================================
CREATE DATABASE IF NOT EXISTS wingify_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wingify_db;

-- Users
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(120) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Categories
CREATE TABLE IF NOT EXISTS categories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80) NOT NULL UNIQUE,
  slug VARCHAR(80) NOT NULL UNIQUE,
  description VARCHAR(255)
);

-- Products
CREATE TABLE IF NOT EXISTS products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  brand VARCHAR(80),
  price DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  image_url VARCHAR(500),
  category_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (category_id) REFERENCES categories(id),
  INDEX idx_product_name (name),
  INDEX idx_product_category (category_id)
);

-- Cart
CREATE TABLE IF NOT EXISTS carts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL UNIQUE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cart_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  cart_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id),
  UNIQUE KEY uk_cart_product (cart_id, product_id)
);

-- Orders
CREATE TABLE IF NOT EXISTS orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  status ENUM('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') NOT NULL DEFAULT 'PENDING',
  shipping_address VARCHAR(500) NOT NULL,
  payment_method VARCHAR(40) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS order_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Seed
INSERT IGNORE INTO categories (id,name,slug,description) VALUES
  (1,'Skincare','skincare','Cleansers, moisturisers, serums'),
  (2,'Makeup','makeup','Lips, eyes, face'),
  (3,'Haircare','haircare','Shampoos, conditioners, oils'),
  (4,'Fragrance','fragrance','Perfumes & body mists');

INSERT IGNORE INTO products (id,name,description,brand,price,stock,image_url,category_id) VALUES
  (1,'Vitamin C Glow Serum','Brightening serum with 10% Vitamin C','Wingify',799.00,50,'https://picsum.photos/seed/p1/400',1),
  (2,'Matte Liquid Lipstick','Long-lasting matte finish','Wingify',499.00,100,'https://picsum.photos/seed/p2/400',2),
  (3,'Argan Hair Oil','Nourishing oil for shiny hair','Wingify',649.00,80,'https://picsum.photos/seed/p3/400',3),
  (4,'Rose Eau de Parfum','Floral fragrance, 50ml','Wingify',1299.00,30,'https://picsum.photos/seed/p4/400',4);

-- Default admin (password: admin123 — bcrypt-encoded by app on first manual insert via API)
