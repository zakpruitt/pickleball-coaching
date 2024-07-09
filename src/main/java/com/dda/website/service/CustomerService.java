package com.dda.website.service;

import com.dda.website.model.CustomerOrder;
import com.dda.website.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerOrder saveOrder(CustomerOrder customerOrder) {
        return customerRepository.save(customerOrder);
    }

    public Optional<CustomerOrder> findOrderById(Long id) {
        return customerRepository.findById(id);
    }
}