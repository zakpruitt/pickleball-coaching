package com.dda.website.service;

import com.dda.website.model.CustomerOrder;
import com.dda.website.model.Payment;
import com.dda.website.model.VideoFileMetadata;
import com.dda.website.repository.CustomerOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrder saveOrder(CustomerOrder customerOrder) {
        return customerOrderRepository.save(customerOrder);
    }

    public CustomerOrder findOrderById(Long id) {
        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer Order not found!"));
    }

    public void setVideoFileMetadata(CustomerOrder customerOrder, VideoFileMetadata videoFileMetadata) {
        customerOrder.setVideoFileMetadata(videoFileMetadata);
        customerOrderRepository.save(customerOrder);
    }

    public void setPayment(CustomerOrder customerOrder, Payment payment) {
        customerOrder.setPayment(payment);
        customerOrderRepository.save(customerOrder);
    }

}