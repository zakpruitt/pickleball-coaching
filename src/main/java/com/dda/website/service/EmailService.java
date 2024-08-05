package com.dda.website.service;

import com.dda.website.model.CustomerOrder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;
    private TemplateEngine templateEngine;

    public void sendOrderReceivedEmail(CustomerOrder customerOrder) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("pruittzn@gmail.com");
        helper.setTo(customerOrder.getEmail());
        helper.setSubject("Order Received");

        Context context = new Context();
        context.setVariable("customerOrder", customerOrder);

        String htmlContent = templateEngine.process("order-received-email-template.html", context);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }

    public void sendErrorOccurredEmail(CustomerOrder customerOrder, String errorMessage) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("pruittzn@gmail.com");
        helper.setTo(customerOrder.getEmail());
        helper.setSubject("Error Occurred");

        Context context = new Context();
        context.setVariable("customerOrder", customerOrder);
        context.setVariable("errorMessage", errorMessage);

        String htmlContent = templateEngine.process("error-occurred-template.html", context);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }

    public void sendOrderConfirmationEmail(CustomerOrder customerOrder) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("pruittzn@gmail.com");
        helper.setTo(customerOrder.getEmail());
        helper.setSubject("Order Confirmation");

        Context context = new Context();
        context.setVariable("customerOrder", customerOrder);

        String htmlContent = templateEngine.process("order-confirmation-template.html", context);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }
}