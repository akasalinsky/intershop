package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String showCart(Model model) {
        return "cart";
    }
}