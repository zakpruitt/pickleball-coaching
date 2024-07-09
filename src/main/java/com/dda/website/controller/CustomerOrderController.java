package com.dda.website.controller;

import com.dda.website.model.CustomerOrder;
import com.dda.website.service.CustomerService;
import com.dda.website.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class CustomerOrderController {
    private final CustomerService customerService;
    private final StripeService stripeService;

    @PostMapping("/create")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public CustomerOrder createCustomerOrder(@RequestBody CustomerOrder customerOrder) {
        return customerService.saveOrder(customerOrder);
    }

    @PostMapping("/payment")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody Map<String, Object> paymentInfo) {
        try {
            Long amount = ((Number) paymentInfo.get("amount")).longValue();
            String currency = (String) paymentInfo.get("currency");

            String successUrl = "http://127.0.0.1:5500/success.html"; // Update with your success URL
            String cancelUrl = "http://127.0.0.1:5500/cancel.html"; // Update with your cancel URL

            Session session = stripeService.createCheckoutSession(amount, currency, successUrl, cancelUrl);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("checkoutUrl", session.getUrl());

            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/update-payment-status")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> paymentStatus) {
        //customerService.updatePaymentStatus(id, paymentStatus.get("paymentStatus"));
        return ResponseEntity.ok().build();
    }
}
