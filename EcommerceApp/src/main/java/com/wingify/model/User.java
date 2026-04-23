package com.wingify.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(name="full_name", nullable=false) private String fullName;
    @Email @NotBlank @Column(nullable=false, unique=true) private String email;
    @NotBlank @Column(nullable=false) private String password;
    private String phone;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role = Role.USER;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
}
