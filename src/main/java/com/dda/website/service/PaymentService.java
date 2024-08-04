package com.dda.website.service;

import com.dda.website.model.types.PaymentProcessingTypes;
import com.dda.website.model.Payment;
import com.dda.website.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.persistence.EntityNotFoundException;
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

    public Payment findPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found!"));
    }

    @Transactional
    public Payment createPayment(Payment payment) {
        try {
            Session session = stripeService.createCheckoutSession(payment.getCurrencyAmount(), payment.getCurrencyType(), SUCCESS_URL, CANCEL_URL);
            payment.setStripePaymentId(session.getId());
            payment.setStripePaymentUrl(session.getUrl());
            payment.setStatus(PaymentProcessingTypes.IN_PROGRESS);
            return paymentRepository.save(payment);
        } catch (StripeException e) {
            return failPayment(payment);
        }
    }

    @Transactional
    public Payment completePayment(Payment payment) {
        payment.setStatus(PaymentProcessingTypes.COMPLETED);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment failPayment(Payment payment) {
        payment.setStatus(PaymentProcessingTypes.FAILED);
        return paymentRepository.save(payment);
    }

}
