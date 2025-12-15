package com.integration.crmerpsync.Repository;

import com.integration.crmerpsync.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);

    Page<Customer> findByUpdatedAtAfter(Instant updatedSince, Pageable pageable);
}
