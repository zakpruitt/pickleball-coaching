package com.dda.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }
}
