package com.wingify.dto;
import jakarta.validation.constraints.*;
public class OrderDtos {
    public record CheckoutRequest(
        @NotBlank String shippingAddress,
        @NotBlank String paymentMethod) {}
    public record StatusUpdateRequest(@NotBlank String status) {}
}
