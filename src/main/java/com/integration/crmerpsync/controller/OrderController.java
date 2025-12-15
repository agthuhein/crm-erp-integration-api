package com.integration.crmerpsync.controller;

import com.integration.crmerpsync.DTO.OrderCreate;
import com.integration.crmerpsync.DTO.OrderResponse;
import com.integration.crmerpsync.entity.Order;
import com.integration.crmerpsync.enums.OrderStatus;
import com.integration.crmerpsync.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crm")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public Order createOrder(@Valid @RequestBody OrderCreate orderCreate) {
        return orderService.createOrder(orderCreate);
    }

    @GetMapping("/order")
    public Page<OrderResponse> getAllOrders(@RequestParam(required=false) OrderStatus status,
                                            @RequestParam(required=false) Instant from,
                                            @RequestParam(required=false) Instant to,
                                            @RequestParam(defaultValue="0") int page,
                                            @RequestParam(defaultValue="10") int size) {
        return orderService.findAllOrdersDto(status, from, to, page, size);
    }
}
