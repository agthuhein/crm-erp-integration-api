package com.integration.crmerpsync.service;

import com.integration.crmerpsync.DTO.OrderCreate;
import com.integration.crmerpsync.Repository.CustomerRepository;
import com.integration.crmerpsync.Repository.OrderRepository;
import com.integration.crmerpsync.Repository.ProductRepository;
import com.integration.crmerpsync.entity.Customer;
import com.integration.crmerpsync.entity.Order;
import com.integration.crmerpsync.entity.OrderItem;
import com.integration.crmerpsync.entity.Product;
import com.integration.crmerpsync.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomerService customerService;

    @Transactional
    public Order createOrder(OrderCreate orderCreate) {
        Customer customer = customerRepository.findById(orderCreate.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found"));

        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.NEW)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for(OrderCreate.OrderItemRequest item: orderCreate.getItems()) {
            Product p = productRepository.findBySku(item.getSku())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid SKU: " + item.getSku()));

            BigDecimal unitPrice = p.getUnitPrice();
            BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(p)
                    .quantity(item.getQuantity())
                    .unitPrice(unitPrice)
                    .lineTotal(total)
                    .build();

            order.getItems().add(orderItem);
            totalAmount = totalAmount.add(total);
        }
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }
}
