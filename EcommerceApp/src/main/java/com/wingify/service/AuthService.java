package com.wingify.service;
import com.wingify.dto.AuthDtos.*;
import com.wingify.exception.ApiException;
import com.wingify.model.*;
import com.wingify.repository.UserRepository;
import com.wingify.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthResponse register(RegisterRequest r) {
        if (users.existsByEmail(r.email())) throw new ApiException("Email already registered");
        var u = User.builder()
            .fullName(r.fullName()).email(r.email())
            .password(encoder.encode(r.password()))
            .phone(r.phone()).role(Role.USER).build();
        users.save(u);
        return new AuthResponse(jwt.generate(u.getEmail(), u.getRole().name()),
            u.getEmail(), u.getFullName(), u.getRole().name());
    }

    public AuthResponse login(LoginRequest r) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(r.email(), r.password()));
        } catch (Exception e) { throw new ApiException("Invalid email or password"); }
        var u = users.findByEmail(r.email()).orElseThrow(() -> new ApiException("User not found"));
        return new AuthResponse(jwt.generate(u.getEmail(), u.getRole().name()),
            u.getEmail(), u.getFullName(), u.getRole().name());
    }
}
