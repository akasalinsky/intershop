package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Mono<Order> createOrder(Cart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Cart is null or empty"));
        }

        Order order = new Order(cart);
        return orderRepository.save(order);
    }
}