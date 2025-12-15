package com.integration.crmerpsync.service;

import com.integration.crmerpsync.DTO.OrderCreate;
import com.integration.crmerpsync.DTO.OrderResponse;
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


    //Create orddder
    @Transactional
    public Order createOrder(OrderCreate orderCreate) {
        Customer customer = customerRepository.findById(orderCreate.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.NEW)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderCreate.OrderItemRequest item : orderCreate.getItems()) {
            Product product = productRepository.findBySku(item.getSku())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid SKU: " + item.getSku()));

            BigDecimal lineTotal =
                    product.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            order.getItems().add(OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .unitPrice(product.getUnitPrice())
                    .lineTotal(lineTotal)
                    .build());

            total = total.add(lineTotal);
        }

        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    @Transactional
    public OrderResponse createOrderDto(OrderCreate orderCreate) {
        Order order = createOrder(orderCreate);
        return toDto(order);
    }

    //get all orders
    public Page<Order> findAllOrders(
            OrderStatus status,
            Instant from,
            Instant to,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (status != null && from != null && to != null)
            return orderRepository.findByStatusAndCreatedAtBetween(status, from, to, pageable);

        if (from != null && to != null)
            return orderRepository.findByCreatedAtBetween(from, to, pageable);

        if (status != null)
            return orderRepository.findByStatus(status, pageable);

        return orderRepository.findAll(pageable);
    }

    public Page<OrderResponse> findAllOrdersDto(
            OrderStatus status,
            Instant from,
            Instant to,
            int page,
            int size
    ) {
        return findAllOrders(status, from, to, page, size).map(this::toDto);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    //set order statussss
    @Transactional
    public Order updateStatus(Long id, OrderStatusUpdate dto) {
        Order order = getOrder(id);

        if (!isValidTransition(order.getStatus(), dto.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid transition " + order.getStatus() + " -> " + dto.getStatus()
            );
        }

        order.setStatus(dto.getStatus());
        return orderRepository.save(order);
    }

    @Transactional
    public Order acknowledgeOrder(Long id) {
        Order order = getOrder(id);

        if (order.getStatus() != OrderStatus.NEW) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only NEW orders can be acknowledged"
            );
        }

        order.setStatus(OrderStatus.PROCESSING);
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
    public Page<Order> getNewOrders(int limit){
        Pageable pg = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return orderRepository.findByStatus(OrderStatus.NEW, pg);
    }

    private OrderResponse toDto(Order o) {
        return new OrderResponse(
                o.getId(),
                o.getCustomer().getId(),
                o.getCustomer().getName(),
                o.getStatus(),
                o.getTotalAmount(),
                o.getCreatedAt()
        );
    }
}
