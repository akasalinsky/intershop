package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/create")
    public Mono<String> createOrder(WebSession session) {
        Cart cart = session.getAttributeOrDefault("cart", new Cart());
        if (!(cart instanceof Cart) || cart.getItems().isEmpty()) {
            return Mono.just("redirect:/cart");
        }

        return orderService.createOrder(cart)
                .map(order -> {
                    session.getAttributes().remove("cart");
                    return "redirect:/orders/" + order.getId();
                })
                .onErrorResume(throwable -> {
                    System.err.println("Error creating order: " + throwable.getMessage());
                    return Mono.just("redirect:/cart");
                });
    }

    @GetMapping("/{orderId}")
    public Mono<String> viewOrder(@PathVariable Long orderId, Model model) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    model.addAttribute("order", order);
                    return Mono.just("order");
                })
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("Order not found with id: " + orderId);
                    return Mono.just("redirect:/orders");
                }));
    }

    @GetMapping
    public Mono<String> listOrders(Model model) {
        return orderRepository.findAll()
                .collectList()
                .doOnNext(orders -> model.addAttribute("orders", orders))
                .then(Mono.just("orders"))
                .onErrorResume(throwable -> {
                    System.err.println("Error fetching orders: " + throwable.getMessage());
                    model.addAttribute("orders", List.of());
                    return Mono.just("orders");
                });
    }
}