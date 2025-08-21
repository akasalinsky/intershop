package com.example.demo.paymentservice.api;

import com.example.demo.payment.client.model.Balance;
import com.example.demo.payment.client.model.PaymentRequest;
import com.example.demo.payment.client.model.PaymentResponse;
import com.example.demo.payment.client.api.DefaultApi;
import com.example.demo.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
public class PaymentsApiController { // НЕ implements com.example.demo.payment.client.api.PaymentsApi

    // ВНЕДРЯЕМ СГЕНЕРИРОВАННЫЙ КЛИЕНТ
    private final DefaultApi paymentClient; // <-- Сгенерированный клиент

    // ВНЕДРЯЕМ ТВОЙ СЕРВИС
    private final PaymentService paymentService; // <-- Твой сервис

    @Autowired
    public PaymentsApiController(DefaultApi paymentClient, PaymentService paymentService) {
        this.paymentClient = paymentClient;
        this.paymentService = paymentService;
    }

    // Метод для получения баланса, вызывая внешний сервис
    public Mono<ResponseEntity<Balance>> getBalance() {
        // Вызываем СГЕНЕРИРОВАННЫЙ клиентский метод
        return paymentClient.getBalance() // <-- Это Mono<Balance> из сгенерированного клиента
                .map(ResponseEntity::ok)
                .onErrorResume(throwable -> {
                    System.err.println("Error fetching balance from payment service: " + throwable.getMessage());
                    // Возвращаем 503 Service Unavailable в случае ошибки вызова сервиса
                    return Mono.just(ResponseEntity.status(503).build());
                });
    }

    // Метод для обработки платежа, вызывая внешний сервис
    public Mono<ResponseEntity<PaymentResponse>> processPayment(Mono<PaymentRequest> paymentRequestMono) {
        return paymentRequestMono
                .flatMap(paymentRequest -> {
                    // Проверяем, что запрос не пустой
                    if (paymentRequest == null) {
                        return Mono.error(new IllegalArgumentException("Payment request body is missing"));
                    }
                    // Проверяем обязательные поля (если нужно)
                    if (paymentRequest.getOrderId() == null || paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                        return Mono.error(new IllegalArgumentException("Invalid payment request: orderId and positive amount are required"));
                    }
                    // Вызываем СГЕНЕРИРОВАННЫЙ клиентский метод
                    return paymentClient.processPayment(paymentRequest); // <-- Это Mono<PaymentResponse> из сгенерированного клиента
                })
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, ex -> {
                    // Логируем ошибку валидации
                    System.err.println("Invalid payment request: " + ex.getMessage());
                    // Создаем ответ об ошибке
                    PaymentResponse errorResponse = new PaymentResponse();
                    errorResponse.setStatus(PaymentResponse.StatusEnum.ERROR);
                    errorResponse.setMessage("Invalid payment request: " + ex.getMessage());
                    // Возвращаем 400 Bad Request для ошибок валидации
                    return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                })
                .onErrorResume(throwable -> {
                    // Логируем любую другую ошибку (например, недоступность сервиса)
                    System.err.println("Error processing payment: " + throwable.getMessage());
                    // Создаем ответ об ошибке
                    PaymentResponse errorResponse = new PaymentResponse();
                    errorResponse.setStatus(PaymentResponse.StatusEnum.ERROR);
                    errorResponse.setMessage("Internal server error");
                    // Возвращаем 503 Service Unavailable для внутренних ошибок сервиса
                    return Mono.just(ResponseEntity.status(503).body(errorResponse));
                });
    }
}