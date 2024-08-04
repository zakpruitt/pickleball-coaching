package com.dda.website.controller;

import com.dda.website.service.EmailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class WebPageController {
    private final EmailServiceImpl emailService;

    @GetMapping("/")
    public String index() {
        emailService.sendSimpleMessage("pruittzn@gmail.com", "basuga", "awesome");
        return "index";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }
}
