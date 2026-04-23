package com.wingify.controller;
import com.wingify.model.Product;
import com.wingify.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController @RequestMapping("/api/products") @RequiredArgsConstructor
public class ProductController {
    private final ProductService products;

    @GetMapping
    public Page<Product> list(
        @RequestParam(required=false) String q,
        @RequestParam(required=false) Long categoryId,
        @RequestParam(required=false) BigDecimal minPrice,
        @RequestParam(required=false) BigDecimal maxPrice,
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="12") int size) {
        return products.search(q, categoryId, minPrice, maxPrice, page, size);
    }

    @GetMapping("/{id}") public Product get(@PathVariable Long id) { return products.get(id); }
}
