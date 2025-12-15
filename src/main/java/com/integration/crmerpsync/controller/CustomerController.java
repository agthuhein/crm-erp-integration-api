package com.integration.crmerpsync.controller;

import com.integration.crmerpsync.DTO.CustomerCreate;
import com.integration.crmerpsync.DTO.CustomerUpdate;
import com.integration.crmerpsync.entity.Customer;
import com.integration.crmerpsync.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crm")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/customer")
    public Customer createCustomer(@Valid @RequestBody CustomerCreate custCreate) {
        return customerService.create(custCreate);
    }

    @GetMapping("/customer")
    public Page<Customer> getCustomers(@RequestParam(defaultValue="0") int page,
                                       @RequestParam(defaultValue="10") int size,
                                       @RequestParam(required=false) Instant updatedAt) {
        return customerService.customers(page, size, updatedAt);

    }

    @PutMapping("/customer/{id}")
    public Customer updateCustomer(@PathVariable("id") Long id,
                                   @Valid @RequestBody CustomerUpdate customerUpdate) {
        return customerService.update(id, customerUpdate);
    }

}
