// src/main/java/com/example/demo/controller/CartController.java
package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    // Просмотр корзины
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Cart cart = getCart(session);
        model.addAttribute("cart", cart);
        return "cart"; // имя файла cart.html
    }

    // Добавление товара в корзину
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null && quantity > 0) {
            Cart cart = getCart(session);
            cart.addItem(product, quantity);
            session.setAttribute("cart", cart); // Обновляем корзину в сессии
        }
        // Перенаправляем обратно на главную или на страницу товара
        return "redirect:/products";
    }

    // Обновление количества товара в корзине
    @PostMapping("/update/{productId}")
    public String updateCart(@PathVariable Long productId,
                             @RequestParam int quantity,
                             HttpSession session) {
        Cart cart = getCart(session);
        cart.updateItemQuantity(productId, quantity);
        session.setAttribute("cart", cart); // Обновляем корзину в сессии
        return "redirect:/cart";
    }

    // Удаление товара из корзины
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session) {
        Cart cart = getCart(session);
        cart.removeItem(productId);
        session.setAttribute("cart", cart); // Обновляем корзину в сессии
        return "redirect:/cart";
    }

    // Вспомогательный метод для получения корзины из сессии
    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}