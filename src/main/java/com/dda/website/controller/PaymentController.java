package com.dda.website.controller;

import com.dda.website.model.CustomerOrder;
import com.dda.website.model.Payment;
import com.dda.website.service.CustomerOrderService;
import com.dda.website.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final CustomerOrderService customerOrderService;

    @PostMapping("/")
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        CustomerOrder customerOrder = customerOrderService.findOrderById(payment.getCustomerOrder().getId());
        payment.setCustomerOrder(customerOrder);

        Payment createdPayment = paymentService.createPayment(payment);
        customerOrderService.setPayment(customerOrder, createdPayment);
        if ("Failed".equals(createdPayment.getStatus())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(createdPayment);
    }

    @PutMapping("/complete")
    public ResponseEntity<Void> completePayment(@RequestBody Long paymentId) {
        Payment payment = paymentService.findPaymentById(paymentId);
        paymentService.completePayment(payment);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/fail")
    public ResponseEntity<Void> failPayment(@RequestBody Long paymentId) {
        Payment payment = paymentService.findPaymentById(paymentId);
        paymentService.failPayment(payment);
        return ResponseEntity.noContent().build();
    }

}
