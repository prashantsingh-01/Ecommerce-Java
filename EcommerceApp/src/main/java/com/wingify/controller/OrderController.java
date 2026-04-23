package com.wingify.controller;
import com.wingify.dto.OrderDtos.CheckoutRequest;
import com.wingify.model.Order;
import com.wingify.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/orders") @RequiredArgsConstructor
public class OrderController {
    private final OrderService orders;

    @PostMapping("/checkout")
    public Order checkout(@AuthenticationPrincipal UserDetails u, @Valid @RequestBody CheckoutRequest r) {
        return orders.checkout(u.getUsername(), r.shippingAddress(), r.paymentMethod());
    }

    @GetMapping("/me") public List<Order> mine(@AuthenticationPrincipal UserDetails u) { return orders.myOrders(u.getUsername()); }
}
