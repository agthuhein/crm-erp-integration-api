package com.integration.crmerpsync.service;

import com.integration.crmerpsync.DTO.OrderCreate;
import com.integration.crmerpsync.DTO.OrderStatusUpdate;
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
import java.time.Instant;

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

    public Page<Order> findAllOrders(OrderStatus orderStatus, Instant from, Instant to, int page, int size ) {
        Pageable pg = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if(from != null && to != null && orderStatus != null) {
            return orderRepository.findByStatusAndCreatedAtBetween(orderStatus, from, to, pg);
        }
        if(from != null && to != null) {
            return orderRepository.findByCreatedAtBetween(from, to, pg);
        }
        if(orderStatus != null) {
            return orderRepository.findByStatus(orderStatus, pg);
        }
        return orderRepository.findAll(pg);
    }

    public Order getOrder(Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    public Order updateStatus(Long id, OrderStatusUpdate orderStatusUpdate) {
        Order order = getOrder(id);
        OrderStatus old = order.getStatus();
        OrderStatus newStatus = orderStatusUpdate.getStatus();

        if(!isValidTransition(newStatus, old)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transition" + old + " to " + newStatus);
        }
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case NEW -> next == OrderStatus.PROCESSING || next == OrderStatus.CANCELLED;
            case PROCESSING -> next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
            case SHIPPED -> next == OrderStatus.INVOICED;
            default -> false;
        };
    }

    /*private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        if (current == OrderStatus.NEW) return next == OrderStatus.PROCESSING || next == OrderStatus.CANCELLED;
        if (current == OrderStatus.PROCESSING) return next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
        if (current == OrderStatus.SHIPPED) return next == OrderStatus.INVOICED;
        if (current == OrderStatus.INVOICED) return false; // terminal
        if (current == OrderStatus.CANCELLED) return false; // terminal
        return false;
    }*/


}
