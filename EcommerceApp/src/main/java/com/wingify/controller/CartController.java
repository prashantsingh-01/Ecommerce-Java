package com.wingify.controller;
import com.wingify.dto.CartDtos.*;
import com.wingify.model.Cart;
import com.wingify.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/cart") @RequiredArgsConstructor
public class CartController {
    private final CartService cart;

    @GetMapping public Cart get(@AuthenticationPrincipal UserDetails u) { return cart.getOrCreate(u.getUsername()); }

    @PostMapping("/items")
    public Cart add(@AuthenticationPrincipal UserDetails u, @Valid @RequestBody AddToCartRequest r) {
        return cart.addItem(u.getUsername(), r.productId(), r.quantity());
    }

    @PutMapping("/items/{itemId}")
    public Cart update(@AuthenticationPrincipal UserDetails u, @PathVariable Long itemId, @Valid @RequestBody UpdateQuantityRequest r) {
        return cart.updateQuantity(u.getUsername(), itemId, r.quantity());
    }

    @DeleteMapping("/items/{itemId}")
    public Cart remove(@AuthenticationPrincipal UserDetails u, @PathVariable Long itemId) {
        return cart.removeItem(u.getUsername(), itemId);
    }
}
