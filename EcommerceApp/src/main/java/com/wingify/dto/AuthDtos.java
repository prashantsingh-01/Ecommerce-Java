package com.wingify.dto;
import jakarta.validation.constraints.*;
public class AuthDtos {
    public record RegisterRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @Size(min=6) String password,
        String phone) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record AuthResponse(String token, String email, String fullName, String role) {}
}
