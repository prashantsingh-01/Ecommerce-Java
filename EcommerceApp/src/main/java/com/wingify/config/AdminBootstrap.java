package com.wingify.config;
import com.wingify.model.*;
import com.wingify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @Override public void run(String... args) {
        if (!users.existsByEmail("admin@wingify.com")) {
            users.save(User.builder()
                .fullName("Wingify Admin")
                .email("admin@wingify.com")
                .password(encoder.encode("admin123"))
                .role(Role.ADMIN).build());
            System.out.println(">> Default admin created: admin@wingify.com / admin123");
        }
    }
}
