package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {
    private List<Order> orders = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    public Order createOrder(Cart cart) {
        Long id = idGenerator.getAndIncrement();
        List<com.example.demo.model.CartItem> items = new ArrayList<>(cart.getItems());
        BigDecimal totalPrice = cart.getTotalPrice();
        Order order = new Order(id, items, totalPrice);
        orders.add(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}