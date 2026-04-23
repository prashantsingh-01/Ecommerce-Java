package com.wingify.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name="orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="user_id", nullable=false) private User user;
    @Column(name="total_amount", nullable=false) private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private OrderStatus status = OrderStatus.PENDING;
    @Column(name="shipping_address", length=500, nullable=false) private String shippingAddress;
    @Column(name="payment_method", nullable=false) private String paymentMethod;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<OrderItem> items = new ArrayList<>();
}
