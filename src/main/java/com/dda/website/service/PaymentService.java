package com.dda.website.service;

import com.dda.website.model.CustomerOrder;
import com.dda.website.model.Payment;
import com.dda.website.repository.CustomerOrderRepository;
import com.dda.website.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PaymentService {

    private final String SUCCESS_URL = "http://localhost:8080/success";
    private final String CANCEL_URL = "http://localhost:8080/cancel";

    private final StripeService stripeService;
    private final PaymentRepository paymentRepository;
    private final CustomerOrderRepository customerOrderRepository;

    @Transactional
    public Payment createPayment(Payment payment) {
        try {


            Session session = stripeService.createCheckoutSession(payment.getCurrencyAmount(), payment.getCurrencyType(), SUCCESS_URL, CANCEL_URL);
            payment.setStripePaymentId(session.getId());
            payment.setStripePaymentUrl(session.getUrl());
            payment.setPaymentStatus("Pending");

            return paymentRepository.save(payment);
        } catch (StripeException e) {
            payment.setPaymentStatus("Failed");
            return paymentRepository.save(payment);
        }
    }

    @Transactional
    public Payment finalizePayment(Long paymentId, CustomerOrder customerOrder) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));
        customerOrderRepository.save(customerOrder);
        payment.setCustomerOrder(customerOrder);
        payment.setPaymentStatus("Success");
        return paymentRepository.save(payment);
    }

    public void updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
