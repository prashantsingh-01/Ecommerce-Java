package com.wingify.service;
import com.wingify.exception.ApiException;
import com.wingify.model.*;
import com.wingify.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class CartService {
    private final CartRepository carts;
    private final UserRepository users;
    private final ProductRepository products;

    @Transactional
    public Cart getOrCreate(String email) {
        var u = users.findByEmail(email).orElseThrow(() -> new ApiException("User not found"));
        return carts.findByUserId(u.getId()).orElseGet(() -> carts.save(Cart.builder().user(u).build()));
    }

    @Transactional
    public Cart addItem(String email, Long productId, int qty) {
        var cart = getOrCreate(email);
        var product = products.findById(productId).orElseThrow(() -> new ApiException("Product not found"));
        if (product.getStock() < qty) throw new ApiException("Insufficient stock");
        var existing = cart.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(productId)).findFirst();
        if (existing.isPresent()) existing.get().setQuantity(existing.get().getQuantity() + qty);
        else cart.getItems().add(CartItem.builder().cart(cart).product(product).quantity(qty).build());
        return carts.save(cart);
    }

    @Transactional
    public Cart updateQuantity(String email, Long itemId, int qty) {
        var cart = getOrCreate(email);
        var item = cart.getItems().stream().filter(i -> i.getId().equals(itemId)).findFirst()
            .orElseThrow(() -> new ApiException("Item not found"));
        item.setQuantity(qty);
        return carts.save(cart);
    }

    @Transactional
    public Cart removeItem(String email, Long itemId) {
        var cart = getOrCreate(email);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        return carts.save(cart);
    }

    @Transactional
    public void clear(String email) {
        var cart = getOrCreate(email);
        cart.getItems().clear();
        carts.save(cart);
    }
}
