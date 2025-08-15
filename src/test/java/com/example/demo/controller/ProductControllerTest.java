// src/test/java/com/example/demo/controller/ProductControllerTest.java
package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void shouldReturnMainPage() throws Exception {
        List<Product> mockProductsList = Arrays.asList(
                new Product(1L, "Тестовый товар 1", "Описание 1", new BigDecimal("100.00"), "/images/test1.jpg"),
                new Product(2L, "Тестовый товар 2", "Описание 2", new BigDecimal("200.00"), "/images/test2.jpg")
        );

        when(productRepository.findAll()).thenReturn(Flux.fromIterable(mockProductsList));

        webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
    }
}