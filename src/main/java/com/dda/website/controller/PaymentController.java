package com.dda.website.controller;

import com.dda.website.model.CustomerOrder;
import com.dda.website.model.Payment;
import com.dda.website.model.VideoFileMetadata;
import com.dda.website.repository.VideoFileMetadataRepository;
import com.dda.website.service.GoogleDriveService;
import com.dda.website.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final GoogleDriveService googleDriveService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody Payment payment) {
        log.info("Starting payment creation " + System.currentTimeMillis());
        Payment createdPayment = paymentService.createPayment(payment);
        log.info("Finished at " + System.currentTimeMillis());
        if ("Failed".equals(createdPayment.getPaymentStatus())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Map<String, String> responseData = new HashMap<>();
        responseData.put("paymentId", createdPayment.getId().toString());
        responseData.put("checkoutUrl", createdPayment.getStripePaymentUrl());

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/finalize")
    public ResponseEntity<Void> finalizePayment(@RequestParam Long paymentId, @RequestBody CustomerOrder customerOrder) {
        try {
            paymentService.finalizePayment(paymentId, customerOrder);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}/update-payment-status")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long id, @RequestBody Map<String, String> paymentStatus) {
        paymentService.updatePaymentStatus(id, paymentStatus.get("paymentStatus"));
        return ResponseEntity.ok().build();
    }
}
