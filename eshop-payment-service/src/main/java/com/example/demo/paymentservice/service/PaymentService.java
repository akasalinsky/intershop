package com.example.demo.paymentservice.service;

import com.example.demo.payment.client.model.PaymentRequest;
import com.example.demo.payment.client.model.PaymentResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private static final BigDecimal FIXED_BALANCE = new BigDecimal("1500.00");

    public Mono<BigDecimal> getBalance() {
        return Mono.just(FIXED_BALANCE);
    }

    public Mono<PaymentResponse> processPayment(PaymentRequest paymentRequest) {
        return getBalance()
                .flatMap(balance -> {
                    BigDecimal amountToPay = paymentRequest.getAmount();
                    if (balance.compareTo(amountToPay) < 0) {
                        return Mono.just(new PaymentResponse()
                                .status(PaymentResponse.StatusEnum.INSUFFICIENT_FUNDS)
                                .message("Insufficient funds on account"));
                    } else {
                        BigDecimal newBalance = balance.subtract(amountToPay);
                        System.out.println("Payment successful for order " + paymentRequest.getOrderId() +
                                ". Amount: " + amountToPay + ". New balance: " + newBalance);
                        return Mono.just(new PaymentResponse()
                                .status(PaymentResponse.StatusEnum.SUCCESS)
                                .message("Payment processed successfully"));
                    }
                });
    }
}