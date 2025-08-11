// src/test/java/com/example/demo/controller/CartControllerTest.java
package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void testViewCart() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cart"));
    }

    @Test
    public void testAddToCart_ProductExists() throws Exception {
        Long productId = 1L;
        Product product = new Product(productId, "Товар", "Описание", new BigDecimal("10.00"), "/image.jpg");
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        mockMvc.perform(post("/cart/add/{productId}", productId)
                        .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        // Проверяем, что метод репозитория был вызван
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testAddToCart_ProductNotFound() throws Exception {
        Long productId = 999L; // Несуществующий ID
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/cart/add/{productId}", productId)
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        // Проверяем, что метод репозитория был вызван
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testUpdateCart() throws Exception {
        Long productId = 1L;
        mockMvc.perform(post("/cart/update/{productId}", productId)
                        .param("quantity", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
        // Здесь сложно проверить состояние сессии без дополнительной настройки,
        // но мы проверили, что редирект произошёл правильно.
    }

    @Test
    public void testRemoveFromCart() throws Exception {
        Long productId = 1L;
        mockMvc.perform(post("/cart/remove/{productId}", productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
        // Аналогично, проверяем редирект.
    }
}