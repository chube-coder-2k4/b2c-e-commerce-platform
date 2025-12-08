package dev.commerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentViewController {

    @GetMapping("/payment-success")
    public String paymentSuccess() {
        return "paysuccess";
    }

    @GetMapping("/payment-failed")
    public String paymentFailed() {
        return "payfail";
    }
}

