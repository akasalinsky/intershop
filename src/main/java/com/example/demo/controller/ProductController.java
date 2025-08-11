// src/main/java/com/example/demo/controller/ProductController.java
package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.PageInfo;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jakarta.servlet.http.HttpSession;
import com.example.demo.model.CartItem;


@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping({"/", "/products"})
    public String listProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session, // Добавляем HttpSession
            Model model) {

        // Получаем все товары из БД
        List<Product> allProducts = productRepository.findAll();

        // Пагинация в памяти
        int totalProducts = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages > 0 ? totalPages : 1;

        int start = (page - 1) * size;
        int end = Math.min(start + size, totalProducts);
        List<Product> productsOnPage = allProducts.subList(start, end);

        PageInfo paging = new PageInfo(size, page, totalPages, totalProducts);

        // Получаем корзину из сессии и создаем карту количеств
        Cart cart = (Cart) session.getAttribute("cart");
        Map<Long, Integer> cartItemQuantity = new HashMap<>();
        if (cart != null) {
            for (CartItem item : cart.getItems()) {
                cartItemQuantity.put(item.getProduct().getId(), item.getQuantity());
            }
        }

        model.addAttribute("products", productsOnPage);
        model.addAttribute("paging", paging);
        model.addAttribute("cartItemQuantity", cartItemQuantity);

        return "main";
    }
}