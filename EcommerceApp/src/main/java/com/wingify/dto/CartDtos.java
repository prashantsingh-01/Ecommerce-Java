package com.wingify.dto;
import jakarta.validation.constraints.*;
public class CartDtos {
    public record AddToCartRequest(@NotNull Long productId, @NotNull @Min(1) Integer quantity) {}
    public record UpdateQuantityRequest(@NotNull @Min(1) Integer quantity) {}
}
