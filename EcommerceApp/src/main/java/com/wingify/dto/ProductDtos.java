package com.wingify.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public class ProductDtos {
    public record ProductRequest(
        @NotBlank String name, String description, String brand,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @NotNull @Min(0) Integer stock,
        String imageUrl,
        @NotNull Long categoryId) {}
}
