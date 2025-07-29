package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private List<CartItem> items;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;

    public Order(Long id, List<CartItem> items, BigDecimal totalPrice) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры
    public Long getId() { return id; }
    public List<CartItem> getItems() { return items; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}