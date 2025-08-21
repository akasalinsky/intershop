package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping({"/", "/products"})
    public Mono<String> listProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            WebSession session,
            Model model) {

        final int validatedSize = Math.max(1, Math.min(size, 100));

        return productRepository.findAll()
                .collectList()
                .map(allProducts -> {
                    int totalProducts = allProducts.size();
                    int totalPages = (int) Math.ceil((double) totalProducts / validatedSize);

                    int validatedPage = page;
                    if (validatedPage < 1) validatedPage = 1;
                    if (totalPages > 0 && validatedPage > totalPages) validatedPage = totalPages;

                    int start = (validatedPage - 1) * validatedSize;
                    start = Math.min(start, totalProducts);
                    int end = Math.min(start + validatedSize, totalProducts);

                    List<Product> productsOnPage = allProducts.subList(start, end);

                    PageInfo paging = new PageInfo(validatedSize, validatedPage, totalPages, (long) totalProducts);

                    // Логика работы с корзиной для отображения количества товаров
                    Cart cart = session.getAttributeOrDefault("cart", new Cart());
                    if (!(cart instanceof Cart)) {
                        cart = new Cart();
                        session.getAttributes().put("cart", cart);
                    }

                    Map<Long, Integer> cartItemQuantity = new HashMap<>();
                    for (CartItem item : cart.getItems()) {
                        cartItemQuantity.put(item.getProduct().getId(), item.getQuantity());
                    }

                    model.addAttribute("products", productsOnPage);
                    model.addAttribute("paging", paging);
                    model.addAttribute("cartItemQuantity", cartItemQuantity);

                    return "main";
                });
    }

    @GetMapping("/products/{id}")
    public Mono<String> viewProduct(@PathVariable Long id, Model model) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    model.addAttribute("product", product);
                    return Mono.just("product-detail");
                })
                .switchIfEmpty(Mono.just("redirect:/products"));
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}