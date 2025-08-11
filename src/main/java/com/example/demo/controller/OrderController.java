// src/main/java/com/example/demo/controller/OrderController.java
package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders") // Изменим базовый путь на /orders
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository; // Добавим репозиторий

    // Создание заказа (POST запрос из формы "Купить" в корзине)
    @PostMapping("/create") // Путь останется /order/create
    public String createOrder(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            // Если корзины нет или она пуста, перенаправляем в корзину
            return "redirect:/cart";
        }

        Order order = orderService.createOrder(cart, session);
        if (order != null) {
            // Перенаправляем на страницу подтверждения заказа
            return "redirect:/orders/" + order.getId();
        } else {
            // Если заказ не создан (например, корзина была пуста)
            return "redirect:/cart";
        }
    }

    // Просмотр конкретного заказа
    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable Long orderId, Model model) {
        // Получаем заказ из репозитория
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            // Если заказ не найден, можно перенаправить на список или показать 404
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "order"; // order.html
    }

    // Список всех заказов
    @GetMapping // Отвечает за /orders
    public String listOrders(Model model) {
        // Получить все заказы из репозитория
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "orders"; // orders.html
    }
}