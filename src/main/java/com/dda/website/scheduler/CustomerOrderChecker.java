package com.dda.website.scheduler;

import com.dda.website.model.CustomerOrder;
import com.dda.website.repository.CustomerOrderRepository;
import com.dda.website.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class CustomerOrderChecker {
    private CustomerOrderRepository customerOrderRepository;
    private EmailService emailService;

    @Scheduled(fixedRate = 60000) // Runs every 10 minutes (600000) --- 60000 = 1 min
    public void checkOrders() {
        log.info("Checking for any new completed orders...");
        List<CustomerOrder> orders = customerOrderRepository.findPendingOrderEmails();
        log.info("Found {0} pending order(s)! Proceeding to send emails out...", orders.size());

        for (CustomerOrder order : orders) {
            try {
                emailService.sendOrderReceivedEmail(order);
                order.setEmailSent(true);
                customerOrderRepository.save(order);
            } catch (Exception e) {
                // Handle exception (e.g., log the error)
            }
        }
    }
}