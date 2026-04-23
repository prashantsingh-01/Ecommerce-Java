package com.wingify.service;
import com.wingify.exception.ApiException;
import com.wingify.model.*;
import com.wingify.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service @RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orders;
    private final UserRepository users;
    private final CartService cartService;
    private final ProductRepository products;

    @Transactional
    public Order checkout(String email, String address, String paymentMethod) {
        var u = users.findByEmail(email).orElseThrow(() -> new ApiException("User not found"));
        var cart = cartService.getOrCreate(email);
        if (cart.getItems().isEmpty()) throw new ApiException("Cart is empty");

        var order = Order.builder()
            .user(u).shippingAddress(address).paymentMethod(paymentMethod)
            .status(OrderStatus.PENDING).totalAmount(BigDecimal.ZERO).build();

        BigDecimal total = BigDecimal.ZERO;
        for (var ci : cart.getItems()) {
            var p = ci.getProduct();
            if (p.getStock() < ci.getQuantity()) throw new ApiException("Out of stock: " + p.getName());
            p.setStock(p.getStock() - ci.getQuantity());
            products.save(p);
            var oi = OrderItem.builder().order(order).product(p)
                .quantity(ci.getQuantity()).price(p.getPrice()).build();
            order.getItems().add(oi);
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }
        order.setTotalAmount(total);
        var saved = orders.save(order);
        cartService.clear(email);
        return saved;
    }

    public List<Order> myOrders(String email) {
        var u = users.findByEmail(email).orElseThrow(() -> new ApiException("User not found"));
        return orders.findByUserIdOrderByCreatedAtDesc(u.getId());
    }

    public List<Order> all() { return orders.findAll(); }

    @Transactional
    public Order updateStatus(Long id, String status) {
        var o = orders.findById(id).orElseThrow(() -> new ApiException("Order not found"));
        try { o.setStatus(OrderStatus.valueOf(status.toUpperCase())); }
        catch (Exception e) { throw new ApiException("Invalid status"); }
        return orders.save(o);
    }
}
