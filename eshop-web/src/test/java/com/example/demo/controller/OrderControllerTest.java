package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@WebFluxTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testListOrders() throws Exception {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setTotalPrice(new BigDecimal("150.00"));
        order1.setCreatedAt(LocalDateTime.now());

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTotalPrice(new BigDecimal("250.00"));
        order2.setCreatedAt(LocalDateTime.now());

        when(orderRepository.findAll()).thenReturn(Flux.fromIterable(Arrays.asList(order1, order2)));

        webTestClient.get().uri("/orders")
                .exchange()
                .expectStatus().isOk();

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testViewOrder_OrderExists() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setTotalPrice(new BigDecimal("150.00"));
        order.setCreatedAt(LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Mono.just(order));

        webTestClient.get().uri("/orders/{orderId}", orderId)
                .exchange()
                .expectStatus().isOk();

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void testViewOrder_OrderNotFound() throws Exception {
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Mono.empty());

        webTestClient.get().uri("/orders/{orderId}", orderId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().value("Location", location -> location.contains("/orders"));

        verify(orderRepository, times(1)).findById(orderId);
    }
}