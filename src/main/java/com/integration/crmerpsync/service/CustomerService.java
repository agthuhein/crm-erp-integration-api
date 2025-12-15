package com.integration.crmerpsync.service;

import com.integration.crmerpsync.DTO.CustomerCreate;
import com.integration.crmerpsync.DTO.CustomerUpdate;
import com.integration.crmerpsync.Repository.CustomerRepository;
import com.integration.crmerpsync.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer create(CustomerCreate create){
        if(customerRepository.existsByEmail(create.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        Customer customer = Customer.builder()
                .name(create.getName())
                .email(create.getEmail())
                .phone(create.getPhone())
                .build();

        return customerRepository.save(customer);
    }

    public Customer update(Long id, CustomerUpdate update){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found."));

        customer.setName(update.getName());
        customer.setPhone(update.getPhone());
        return customerRepository.save(customer);
    }

    public Page<Customer> customers(Integer page, Integer size, Instant updatedAt){
        Pageable pg = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt" ));

        if(updatedAt !=null){
            return customerRepository.findByUpdatedAtAfter(updatedAt, pg);
        }
        return customerRepository.findAll(pg);
    }

    public Customer getById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found."));
    }
}
