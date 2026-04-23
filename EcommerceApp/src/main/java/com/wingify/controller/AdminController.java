package com.wingify.controller;
import com.wingify.dto.OrderDtos.StatusUpdateRequest;
import com.wingify.dto.ProductDtos.ProductRequest;
import com.wingify.model.*;
import com.wingify.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/admin") @RequiredArgsConstructor
public class AdminController {
    private final ProductService products;
    private final OrderService orders;

    @PostMapping("/products") public Product create(@Valid @RequestBody ProductRequest r) { return products.create(r); }
    @PutMapping("/products/{id}") public Product update(@PathVariable Long id, @Valid @RequestBody ProductRequest r) { return products.update(id, r); }
    @DeleteMapping("/products/{id}") public void delete(@PathVariable Long id) { products.delete(id); }

    @GetMapping("/orders") public List<Order> allOrders() { return orders.all(); }
    @PatchMapping("/orders/{id}/status")
    public Order updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest r) {
        return orders.updateStatus(id, r.status());
    }
}
