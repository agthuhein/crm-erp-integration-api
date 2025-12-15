package com.integration.crmerpsync.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.integration.crmerpsync.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=30)
    private OrderStatus status;

    @Column(name="total_amount", nullable=false)
    private BigDecimal totalAmount;

    @CreationTimestamp
    @Column(name="created_at", nullable=false, updatable=false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
}
