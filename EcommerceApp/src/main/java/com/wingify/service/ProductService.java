package com.wingify.service;
import com.wingify.dto.ProductDtos.ProductRequest;
import com.wingify.exception.ApiException;
import com.wingify.model.Product;
import com.wingify.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service @RequiredArgsConstructor
public class ProductService {
    private final ProductRepository products;
    private final CategoryRepository categories;

    public Page<Product> search(String q, Long categoryId, BigDecimal min, BigDecimal max, int page, int size) {
        return products.search(q, categoryId, min, max, PageRequest.of(page, size, Sort.by("id").descending()));
    }
    public Product get(Long id) {
        return products.findById(id).orElseThrow(() -> new ApiException("Product not found"));
    }
    public Product create(ProductRequest r) {
        var cat = categories.findById(r.categoryId()).orElseThrow(() -> new ApiException("Category not found"));
        return products.save(Product.builder()
            .name(r.name()).description(r.description()).brand(r.brand())
            .price(r.price()).stock(r.stock()).imageUrl(r.imageUrl()).category(cat).build());
    }
    public Product update(Long id, ProductRequest r) {
        var p = get(id);
        var cat = categories.findById(r.categoryId()).orElseThrow(() -> new ApiException("Category not found"));
        p.setName(r.name()); p.setDescription(r.description()); p.setBrand(r.brand());
        p.setPrice(r.price()); p.setStock(r.stock()); p.setImageUrl(r.imageUrl()); p.setCategory(cat);
        return products.save(p);
    }
    public void delete(Long id) { products.deleteById(id); }
}
