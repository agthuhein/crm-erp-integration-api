package com.integration.crmerpsync.service;

import com.integration.crmerpsync.DTO.CustomerCreate;
import com.integration.crmerpsync.Repository.CustomerRepository;
import com.integration.crmerpsync.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer create(CustomerCreate request){
        if(customerRepository.existsByEmail(request.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        return customerRepository.save(customer);
    }
}
