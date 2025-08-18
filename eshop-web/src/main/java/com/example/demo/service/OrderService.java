package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import com.example.demo.payment.client.api.DefaultApi;
import com.example.demo.payment.client.model.PaymentRequest;
import com.example.demo.payment.client.model.PaymentResponse;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final DefaultApi paymentsApiClient; // <-- Добавлено

    @Autowired
    public OrderService(OrderRepository orderRepository, DefaultApi paymentsApiClient) { // <-- Изменено
        this.orderRepository = orderRepository;
        this.paymentsApiClient = paymentsApiClient; // <-- Добавлено
    }

    @Autowired
    private OrderRepository orderRepository;

    public Mono<Order> createOrder(Cart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Cart is null or empty"));
        }

        Order order = new Order(cart);

        // 1. Создать заказ в БД
        return orderRepository.save(order)
                .flatMap(savedOrder -> {
                    // 2. Подготовить запрос на оплату
                    PaymentRequest paymentRequest = new PaymentRequest()
                            .orderId(savedOrder.getId())
                            .amount(savedOrder.getTotalPrice());

                    // 3. Вызвать сервис платежей
                    return paymentsApiClient.processPayment(paymentRequest)
                            .flatMap(paymentResponse -> {
                                if (PaymentResponse.StatusEnum.SUCCESS.equals(paymentResponse.getStatus())) {
                                    // 4. Если оплата успешна, вернуть заказ
                                    System.out.println("Payment successful for order " + savedOrder.getId());
                                    return Mono.just(savedOrder);
                                } else {
                                    // 5. Если оплата не удалась, удалить заказ и вернуть ошибку
                                    System.out.println("Payment failed for order " + savedOrder.getId() + ": " + paymentResponse.getMessage());
                                    return orderRepository.deleteById(savedOrder.getId())
                                            .then(Mono.error(new RuntimeException("Payment failed: " + paymentResponse.getMessage())));
                                }
                            });
                });
    }
}