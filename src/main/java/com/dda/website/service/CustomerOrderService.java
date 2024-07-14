package com.dda.website.service;

import com.dda.website.model.CustomerOrder;
import com.dda.website.repository.CustomerOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrder saveOrder(CustomerOrder customerOrder) {
        return customerOrderRepository.save(customerOrder);
    }

    public Optional<CustomerOrder> findOrderById(Long id) {
        return customerOrderRepository.findById(id);
    }
}