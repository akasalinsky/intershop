package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;


@Controller
public class OrderController {

    @GetMapping("/orders")
    public String showOrders(Model model) {
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model) {
        return "order-details";
    }
}
