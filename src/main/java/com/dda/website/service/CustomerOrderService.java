package com.dda.website.service;

import com.dda.website.model.CustomerOrder;
import com.dda.website.model.VideoFileMetadata;
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

    public CustomerOrder setVideoFileMetadataId(VideoFileMetadata videoFileMetadata, Long customerOrderId) {
        CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId).orElseThrow(
                () -> new RuntimeException("CustomerOrder not found")
        );
        customerOrder.setVideoFileMetadata(videoFileMetadata);
        return customerOrderRepository.save(customerOrder);
    }

    public CustomerOrder setVideoFileMetadataId(VideoFileMetadata videoFileMetadata, CustomerOrder customerOrder) {
        customerOrder.setVideoFileMetadata(videoFileMetadata);
        return customerOrderRepository.save(customerOrder);
    }

}