// src/test/java/com/example/demo/controller/OrderControllerTest.java
package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testListOrders() throws Exception {
        // Подготавливаем фиктивные данные
        Order order1 = new Order();
        order1.setId(1L);
        order1.setTotalPrice(new BigDecimal("150.00"));
        order1.setCreatedAt(LocalDateTime.now());

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTotalPrice(new BigDecimal("250.00"));
        order2.setCreatedAt(LocalDateTime.now());

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));
        // Можно добавить проверку размера списка заказов
        // .andExpect(model().attribute("orders", hasSize(2)));
    }

    @Test
    public void testViewOrder_OrderExists() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setTotalPrice(new BigDecimal("150.00"));
        order.setCreatedAt(LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    public void testViewOrder_OrderNotFound() throws Exception {
        Long orderId = 999L; // Несуществующий ID
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));
    }

    // Тест для POST /orders/create сложнее, так как он зависит от сессии и сервиса.
    // Его лучше писать как интеграционный тест с @SpringBootTest.
    // Пока оставим его без теста в @WebMvcTest.
}