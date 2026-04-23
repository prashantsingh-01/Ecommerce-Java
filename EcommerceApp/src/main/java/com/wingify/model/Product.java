package com.wingify.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable=false) private String name;
    @Column(columnDefinition="TEXT") private String description;
    private String brand;
    @NotNull @DecimalMin("0.0") @Column(nullable=false) private BigDecimal price;
    @Min(0) @Column(nullable=false) private Integer stock = 0;
    @Column(name="image_url", length=500) private String imageUrl;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="category_id", nullable=false) private Category category;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
}
