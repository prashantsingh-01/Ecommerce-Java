package com.wingify.controller;
import com.wingify.dto.AuthDtos.*;
import com.wingify.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;
    @PostMapping("/register") public AuthResponse register(@Valid @RequestBody RegisterRequest r) { return auth.register(r); }
    @PostMapping("/login")    public AuthResponse login(@Valid @RequestBody LoginRequest r)       { return auth.login(r); }
}
