package com.example.demo.controller;

import com.example.demo.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@WebFluxTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @Test
    public void testViewCart() throws Exception {
        webTestClient
                .get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testAddToCart_ProductExists() throws Exception {
        Long productId = 1L;
        Product product = new Product(productId, "Товар", "Описание", new BigDecimal("10.00"), "/image.jpg");
        when(productService.getProductById(productId)).thenReturn(Mono.just(product));

        webTestClient
                .post()
                .uri("/cart/add/{productId}?quantity=2", productId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().value("Location", location -> location.contains("/products"));

        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    public void testAddToCart_ProductNotFound() throws Exception {
        Long productId = 999L;
        when(productService.getProductById(productId)).thenReturn(Mono.empty());

        webTestClient
                .post()
                .uri("/cart/add/{productId}?quantity=1", productId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().value("Location", location -> location.contains("/products"));

        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    public void testUpdateCart() throws Exception {
        Long productId = 1L;
        webTestClient
                .post()
                .uri("/cart/update/{productId}?quantity=3", productId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().value("Location", location -> location.contains("/cart"));
    }

    @Test
    public void testRemoveFromCart() throws Exception {
        Long productId = 1L;
        webTestClient
                .post()
                .uri("/cart/remove/{productId}", productId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().value("Location", location -> location.contains("/cart"));
    }
}