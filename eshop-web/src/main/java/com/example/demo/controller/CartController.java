package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public Mono<String> viewCart(WebSession session, Model model) {
        Cart cart = session.getAttributeOrDefault("cart", new Cart());
        if (!(cart instanceof Cart)) {
            cart = new Cart();
            session.getAttributes().put("cart", cart);
        }
        model.addAttribute("cart", cart);
        return Mono.just("cart");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{productId}")
    public Mono<String> addToCart(@PathVariable Long productId,
                                  @RequestParam(defaultValue = "1") int quantity,
                                  WebSession session) {
        if (quantity <= 0) {
            return Mono.just("redirect:/products");
        }

        // Создаем final переменную для использования в лямбде
        final Cart finalCart;
        Object cartObject = session.getAttribute("cart");
        if (cartObject instanceof Cart) {
            finalCart = (Cart) cartObject;
        } else {
            finalCart = new Cart();
            session.getAttributes().put("cart", finalCart);
        }

        return productService.getProductById(productId)
                .switchIfEmpty(Mono.fromRunnable(() -> {
                    System.out.println("Product not found with id: " + productId);
                }))
                .doOnNext(product -> {
                    // Используем final переменную
                    finalCart.addItem(product, quantity);
                    session.getAttributes().put("cart", finalCart);
                })
                .then(Mono.just("redirect:/products"));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{productId}")
    public Mono<String> updateCart(@PathVariable Long productId,
                                   @RequestParam int quantity,
                                   WebSession session) {
        // Используем final переменную
        final Cart finalCart;
        Object cartObject = session.getAttribute("cart");
        if (cartObject instanceof Cart) {
            finalCart = (Cart) cartObject;
        } else {
            finalCart = new Cart();
            session.getAttributes().put("cart", finalCart);
        }

        finalCart.updateItemQuantity(productId, quantity);
        session.getAttributes().put("cart", finalCart);

        return Mono.just("redirect:/cart");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/remove/{productId}")
    public Mono<String> removeFromCart(@PathVariable Long productId, WebSession session) {
        // Используем final переменную
        final Cart finalCart;
        Object cartObject = session.getAttribute("cart");
        if (cartObject instanceof Cart) {
            finalCart = (Cart) cartObject;
        } else {
            finalCart = new Cart();
            session.getAttributes().put("cart", finalCart);
        }

        finalCart.removeItem(productId);
        session.getAttributes().put("cart", finalCart);

        return Mono.just("redirect:/cart");
    }


}