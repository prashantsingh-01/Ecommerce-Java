# Wingify — Beauty Products eCommerce (Spring Boot + MySQL)

Major project backend for **YM College — Final Year**. Java 17 · Spring Boot 3.2 · Spring Security (JWT) · Spring Data JPA · MySQL 8 · Maven.

---

## 1. Project Title
**Wingify — A Beauty Products eCommerce Platform**

## 2. Abstract
Wingify is a full-stack eCommerce web application focused on beauty products (skincare, makeup, haircare, fragrance). The backend is built with Java Spring Boot following the MVC architecture and exposes a secure RESTful API consumed by an HTML/CSS/JS (or React) frontend. MySQL provides relational persistence. The system implements core eCommerce flows: registration/login with JWT authentication and BCrypt password encryption, product catalog with search and category filters, shopping cart, checkout with stock decrement, order management, and an admin dashboard for managing products and orders.

## 3. Objectives
- Build a real-world, production-style eCommerce backend with clean MVC layering.
- Demonstrate secure authentication using JWT + BCrypt.
- Implement product CRUD, dynamic search/filter, cart and order workflows.
- Provide an admin role with dashboard endpoints.
- Deliver complete documentation suitable for academic evaluation.

## 4. System Architecture
```
 ┌──────────────┐     HTTPS/JSON     ┌────────────────────────┐     JDBC     ┌──────────┐
 │  Frontend    │ ─────────────────▶ │  Spring Boot Backend   │ ───────────▶ │  MySQL   │
 │ HTML/CSS/JS  │                    │  (MVC + REST + JWT)    │              │  wingify │
 │  / React     │ ◀───────────────── │  Controllers→Services  │ ◀─────────── │   _db    │
 └──────────────┘     JSON + JWT     │  →Repositories→JPA     │              └──────────┘
                                     └────────────────────────┘
```
**Layers:** Controller (REST) → Service (business rules) → Repository (Spring Data JPA) → Entity (JPA models) ↔ MySQL.
**Security:** Stateless JWT filter validates `Authorization: Bearer <token>` on protected endpoints. `ROLE_ADMIN` gates `/api/admin/**`.

## 5. Database Design
See `sql/schema.sql`. Tables: `users, categories, products, carts, cart_items, orders, order_items`.
Run it once: `mysql -u root -p < sql/schema.sql`.

## 6. Backend Design (package layout)
```
com.wingify
 ├── WingifyApplication.java        # @SpringBootApplication entry point
 ├── config/      SecurityConfig, AdminBootstrap
 ├── controller/  Auth, Product, Category, Cart, Order, Admin
 ├── dto/         Request/Response records
 ├── exception/   ApiException + GlobalExceptionHandler
 ├── model/       JPA entities + enums (Role, OrderStatus)
 ├── repository/  Spring Data JPA interfaces
 ├── security/    JwtService, JwtAuthFilter, CustomUserDetailsService
 └── service/     AuthService, ProductService, CartService, OrderService
```

## 7. REST API Endpoints

### Auth (public)
| Method | URL | Body | Response |
|---|---|---|---|
| POST | `/api/auth/register` | `{ "fullName":"Asha","email":"asha@x.com","password":"secret1","phone":"99999" }` | `{ "token":"...", "email":"asha@x.com", "role":"USER" }` |
| POST | `/api/auth/login` | `{ "email":"asha@x.com","password":"secret1" }` | `{ "token":"...", "role":"USER" }` |

### Catalog (public)
- `GET /api/categories`
- `GET /api/products?q=serum&categoryId=1&minPrice=100&maxPrice=2000&page=0&size=12`
- `GET /api/products/{id}`

### Cart (USER)
- `GET /api/cart`
- `POST /api/cart/items` `{ "productId":1, "quantity":2 }`
- `PUT /api/cart/items/{itemId}` `{ "quantity":3 }`
- `DELETE /api/cart/items/{itemId}`

### Orders (USER)
- `POST /api/orders/checkout` `{ "shippingAddress":"...", "paymentMethod":"COD" }`
- `GET /api/orders/me`

### Admin (ADMIN)
- `POST /api/admin/products`
- `PUT /api/admin/products/{id}`
- `DELETE /api/admin/products/{id}`
- `GET /api/admin/orders`
- `PATCH /api/admin/orders/{id}/status` `{ "status":"SHIPPED" }`

All protected endpoints require header: `Authorization: Bearer <jwt>`.

## 8. Frontend Structure (suggested)
```
/index.html        Home + featured products
/products.html     Catalog with search & category filter
/product.html?id=1 Product detail
/cart.html         Cart view + checkout button
/login.html /register.html
/orders.html       My orders
/admin.html        Admin dashboard (products + orders)
```
Use Bootstrap 5 for styling. Call the REST API via `fetch()` and store the JWT in `localStorage`.

## 9. Key Code Snippets
- **JWT issuance:** `security/JwtService.java`
- **Password hashing + login:** `service/AuthService.java`
- **Product search query:** `repository/ProductRepository.java` (`@Query` with optional filters)
- **Stock-aware checkout:** `service/OrderService.java#checkout`
- **Role-based authorization:** `config/SecurityConfig.java`

## 10. Setup Instructions
**Prerequisites:** JDK 17, Maven 3.9+, MySQL 8.

```bash
# 1. Create database (or let Hibernate auto-create)
mysql -u root -p < sql/schema.sql

# 2. Update credentials if needed
#    src/main/resources/application.properties  -> spring.datasource.username/password

# 3. Build & run
mvn spring-boot:run
# Backend → http://localhost:8080

# 4. Default admin account (auto-created on first start)
#    email: admin@wingify.com   password: admin123
```

**Quick test:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@wingify.com","password":"admin123"}'
```

**React frontend integration:** CORS is pre-configured for `http://localhost:5173` and `http://localhost:3000`. From your React app:
```js
const res = await fetch("http://localhost:8080/api/products");
const page = await res.json();   // { content: [...], totalElements, ... }
```

## 11. Viva Questions & Answers

**Q1. Why Spring Boot for this project?**
A. It provides auto-configuration, embedded Tomcat, starter dependencies, and seamless integration with JPA & Security — drastically reducing boilerplate compared to traditional Spring/Servlets.

**Q2. What is MVC and how is it applied here?**
A. Model (JPA entities) — Controller (REST endpoints) — View (JSON responses consumed by frontend). Business logic lives in a separate Service layer between Controller and Repository for testability.

**Q3. How is the password stored securely?**
A. Hashed using **BCrypt** (`BCryptPasswordEncoder`) with a per-user salt before persisting. Plaintext passwords are never stored or logged.

**Q4. What is JWT and why use it?**
A. JSON Web Token — a signed, stateless token containing user identity and role. The server validates it on each request without maintaining session state, making the API horizontally scalable.

**Q5. How does role-based access work?**
A. On login, the JWT carries a `role` claim. `JwtAuthFilter` populates `SecurityContext` with `ROLE_USER` or `ROLE_ADMIN`. `SecurityConfig` restricts `/api/admin/**` to `hasRole("ADMIN")`.

**Q6. Difference between `@OneToMany` and `@ManyToOne`?**
A. `@ManyToOne` is the owning side (holds the foreign key), e.g. `CartItem → Cart`. `@OneToMany(mappedBy="...")` is the inverse side that exposes the collection.

**Q7. What does `cascade=ALL, orphanRemoval=true` do?**
A. Persisting/deleting a parent (e.g. `Cart`) propagates to children (`CartItem`s). `orphanRemoval` deletes children removed from the collection.

**Q8. How is concurrency handled at checkout?**
A. The `checkout` method is `@Transactional` — stock decrement and order persistence happen atomically; failure rolls back all changes.

**Q9. How would you scale this app?**
A. Stateless JWT allows horizontal scaling behind a load balancer. Add Redis for cart caching, MySQL read replicas for catalog, and move images to object storage (S3).

**Q10. How is input validated?**
A. Bean Validation (`@NotBlank`, `@Email`, `@Min`) on DTOs + `@Valid` on controllers. Errors are caught by `GlobalExceptionHandler` and returned as a structured 400 response.

## 12. Future Enhancements
- Online payments (Razorpay / Stripe) and order invoices (PDF).
- Product reviews, ratings, and wishlist.
- Email notifications (order placed, shipped) via SMTP.
- Image uploads to AWS S3 instead of URL strings.
- Recommendation engine based on browsing history.
- Mobile app (Flutter / React Native) sharing the same REST API.
- Dockerfile + docker-compose (app + MySQL) for one-command deploy.
- CI/CD with GitHub Actions; deploy to AWS Elastic Beanstalk.

---
© 2026 — Wingify Major Project, YM College.
