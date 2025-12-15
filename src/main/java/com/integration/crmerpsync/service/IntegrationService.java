package com.integration.crmerpsync.service;

import java.io.StringWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.integration.crmerpsync.DTO.CustomerBulkImport;
import com.integration.crmerpsync.DTO.ErpOrderStatusWebhook;
import com.integration.crmerpsync.Repository.CustomerRepository;
import com.integration.crmerpsync.Repository.OrderRepository;
import com.integration.crmerpsync.entity.Customer;
import com.integration.crmerpsync.entity.Order;
import com.integration.crmerpsync.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public Map<String, Object> bulkImportCustomers(CustomerBulkImport customerBulkImport) {
        int inserted = 0;
        int skipped = 0;

        for(var c: customerBulkImport.getCustomers()) {
            if(customerRepository.existsByEmail(c.getEmail())) {
                skipped++;
                continue;
            }
            Customer newCustomer = Customer.builder()
                    .name(c.getName())
                    .email(c.getEmail())
                    .phone(c.getPhone())
                    .build();

            customerRepository.save(newCustomer);
            inserted++;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("inserted", inserted);
        result.put("skipped", skipped);
        return result;
    }

    public Order erpOrderStatusWebHook(ErpOrderStatusWebhook erpOrderStatusWebhook) {
        return orderService.updateStatus(erpOrderStatusWebhook.getOrderId(), new com.integration.crmerpsync.DTO.OrderStatusUpdate() {{
            setStatus(erpOrderStatusWebhook.getStatus());
        }});
    }

    public Map<String, Object> metrics(){
        long customers = customerRepository.count();
        long orders = orderRepository.count();
        long newOrders = orderRepository.findByStatus(OrderStatus.NEW,
                org.springframework.data.domain.PageRequest.of(0,1)).getTotalElements();

        Map<String, Object> m = new HashMap<>();
        m.put("customers", customers);
        m.put("orders", orders);
        m.put("newOrders", newOrders);
        return m;
    }

    public String exportOrdersCsv(Instant from, Instant to) {
        if (from == null || to == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "from and to are required");
        }

        List<Order> list = orderRepository.findByCreatedAtBetween(from, to, org.springframework.data.domain.PageRequest.of(0, 5000))
                .getContent();

        StringWriter sw = new StringWriter();
        sw.append("orderId,customerId,status,totalAmount,createdAt\n");
        for (Order o : list) {
            sw.append(o.getId() + "," +
                    o.getCustomer().getId() + "," +
                    o.getStatus() + "," +
                    o.getTotalAmount() + "," +
                    o.getCreatedAt() + "\n");
        }
        return sw.toString();
    }
}
