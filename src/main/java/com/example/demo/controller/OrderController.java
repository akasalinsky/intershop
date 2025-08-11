// src/main/java/com/example/demo/controller/OrderController.java
package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Создание заказа (POST запрос из формы "Купить" в корзине)
    @PostMapping("/create")
    public String createOrder(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            // Если корзины нет, перенаправляем на главную или в корзину
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
        // TODO: Получить заказ из репозитория
        // Order order = orderRepository.findById(orderId).orElse(null);
        // model.addAttribute("order", order);
        // return "order"; // order.html
        return "redirect:/orders"; // Пока просто перенаправляем на список
    }

    // Список всех заказов
    @GetMapping
    public String listOrders(Model model) {
        // TODO: Получить все заказы из репозитория
        // List<Order> orders = orderRepository.findAll();
        // model.addAttribute("orders", orders);
        // return "orders"; // orders.html
        return "redirect:/"; // Пока просто перенаправляем на главную
    }
}