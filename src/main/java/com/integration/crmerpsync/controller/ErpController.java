package com.integration.crmerpsync.controller;

import com.integration.crmerpsync.DTO.OrderStatusUpdate;
import com.integration.crmerpsync.entity.Customer;
import com.integration.crmerpsync.entity.Order;
import com.integration.crmerpsync.enums.OrderStatus;
import com.integration.crmerpsync.service.CustomerService;
import com.integration.crmerpsync.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/erp")
public class ErpController {
    private final OrderService orderService;
    private final CustomerService customerService;

    @GetMapping("/orders")
    public Page<Order> getOrders(@RequestParam(required=false, defaultValue = "NEW") OrderStatus status,
                                  @RequestParam(required=false) Instant from,
                                  @RequestParam(required=false) Instant to,
                                  @RequestParam(defaultValue="0") int page,
                                  @RequestParam(defaultValue="10") int size) {
        return orderService.findAllOrders(status, from, to, page, size);
    }

    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable("id") Long id) {
        return orderService.getOrder(id);
    }

    @PutMapping("/orders/{id}/status")
    public Order updateOrderStatus(@PathVariable("id") Long id, @Valid @RequestBody OrderStatusUpdate updateStatus) {
        return orderService.updateStatus(id, updateStatus);
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomer(@PathVariable("id") Long id) {
        return customerService.getById(id);
    }

}
