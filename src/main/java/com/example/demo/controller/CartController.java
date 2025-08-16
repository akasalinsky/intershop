// src/main/java/com/example/demo/controller/CartController.java
package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public Mono<String> viewCart(WebSession session, Model model) {

        return Mono.justOrEmpty(session.getAttribute("cart"))
                .cast(Cart.class)
                .defaultIfEmpty(new Cart())
                .doOnNext(cart -> model.addAttribute("cart", cart))
                .then(Mono.just("cart"));
    }

    @PostMapping("/add/{productId}")
    public Mono<String> addToCart(@PathVariable Long productId,
                                  @RequestParam(defaultValue = "1") int quantity,
                                  WebSession session) {
        if (quantity <= 0) {
            return Mono.just("redirect:/products");
        }

        Object cartObject = session.getAttribute("cart");
        Cart cart;
        if (cartObject instanceof Cart) {
            cart = (Cart) cartObject;
        } else {
            cart = new Cart();
            session.getAttributes().put("cart", cart);
        }

        return productService.getProductById(productId)
                .switchIfEmpty(Mono.fromRunnable(() -> {
                    System.out.println("Product not found with id: " + productId);
                }))
                .doOnNext(product -> {

                    cart.addItem(product, quantity);
                    session.getAttributes().put("cart", cart);
                })
                .then(Mono.just("redirect:/products"));
    }

    @PostMapping("/update/{productId}")
    public Mono<String> updateCart(@PathVariable Long productId,
                                   @RequestParam int quantity,
                                   WebSession session) {
        Object cartObject = session.getAttribute("cart");
        Cart cart;
        if (cartObject instanceof Cart) {
            cart = (Cart) cartObject;
        } else {
            cart = new Cart();
            session.getAttributes().put("cart", cart);
        }

        cart.updateItemQuantity(productId, quantity);
        session.getAttributes().put("cart", cart);

        return Mono.just("redirect:/cart");
    }

    @PostMapping("/remove/{productId}")
    public Mono<String> removeFromCart(@PathVariable Long productId, WebSession session) {
        Object cartObject = session.getAttribute("cart");
        Cart cart;
        if (cartObject instanceof Cart) {
            cart = (Cart) cartObject;
        } else {
            cart = new Cart();
            session.getAttributes().put("cart", cart);
        }

        cart.removeItem(productId);
        session.getAttributes().put("cart", cart);

        return Mono.just("redirect:/cart");
    }
}