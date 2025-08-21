package com.example.demo.paymentservice.api;

import com.example.demo.payment.client.model.Balance;
import com.example.demo.payment.client.model.PaymentRequest;
import com.example.demo.payment.client.model.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api") // Базовый путь для всех эндпоинтов этого API
public interface PaymentsApi {

    /**
     * Get user balance
     *
     * <p><b>200</b> - Balance retrieved successfully
     * <p><b>500</b> - Internal server error
     * <p><b>503</b> - Service unavailable
     * @return Balance
     */
    @GetMapping("/balance")
    Mono<ResponseEntity<Balance>> getBalance();

    /**
     * Process payment
     *
     * <p><b>200</b> - Payment successful
     * <p><b>400</b> - Bad request (e.g., insufficient funds)
     * <p><b>402</b> - Payment required (alternative for insufficient funds)
     * <p><b>500</b> - Internal server error
     * <p><b>503</b> - Service unavailable
     * @param paymentRequest The paymentRequest parameter
     * @return PaymentResponse
     */
    @PostMapping("/pay")
    Mono<ResponseEntity<PaymentResponse>> processPayment(@RequestBody Mono<PaymentRequest> paymentRequest);
}