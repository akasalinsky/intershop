// src/main/java/com/example/demo/service/OrderService.java
package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Cart cart, HttpSession session) {
        // Проверка внутри сервиса
        if (cart == null || cart.getItems().isEmpty()) {
            return null; // Нельзя создать заказ из пустой или несуществующей корзины
        }

        Order order = new Order(cart);
        Order savedOrder = orderRepository.save(order);

        // Очищаем корзину после оформления заказа
        session.removeAttribute("cart");

        return savedOrder;
    }
}