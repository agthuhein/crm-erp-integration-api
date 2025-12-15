package com.integration.crmerpsync.Repository;

import com.integration.crmerpsync.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import com.integration.crmerpsync.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByCreatedAtBetween(Instant from, Instant to, Pageable pageable);
    Page<Order> findByStatusAndCreatedAtBetween(OrderStatus status, Instant from, Instant to, Pageable pageable);
}
