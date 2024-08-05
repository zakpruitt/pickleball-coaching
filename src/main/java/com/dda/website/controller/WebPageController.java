package com.dda.website.controller;

import com.dda.website.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class WebPageController {
    private final EmailService emailService;

    @GetMapping("/")
    public String index() throws MessagingException {
        //emailService.sendSimpleMessage("pruittzn@gmail.com", "basuga", "awesome");
        return "index";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }
}
