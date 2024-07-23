package com.dda.website.controller;

import com.dda.website.model.CustomerOrder;
import com.dda.website.service.CustomerOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    @PostMapping("/create")
    public ResponseEntity<CustomerOrder> createCustomerOrder(@RequestBody CustomerOrder customerOrder) {
        return ResponseEntity.ok(customerOrderService.saveOrder(customerOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerOrder(@PathVariable Long id) {
        return ResponseEntity.ok(customerOrderService.findOrderById(id));
    }

}
