package com.example.demo.config;

import com.example.demo.payment.client.ApiClient;
import com.example.demo.payment.client.api.DefaultApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PaymentClientConfig {

    @Value("${payment.service.url:http://localhost:8081}")
    private String paymentServiceUrl;

    @Bean
    public WebClient paymentWebClient() {
        return WebClient.builder()
                .baseUrl(paymentServiceUrl)
                .build();
    }

    @Bean
    public ApiClient paymentsApiClient(WebClient paymentWebClient) {
        ApiClient apiClient = new ApiClient(paymentWebClient);
        return apiClient;
    }

    @Bean
    public DefaultApi paymentClient(WebClient paymentWebClient) {
        // Создаем экземпляр сгенерированного ApiClient, передав ему настроенный WebClient
        ApiClient apiClient = new ApiClient(paymentWebClient);
        // Создаем и возвращаем сгенерированный API клиент
        return new DefaultApi(apiClient);
    }
}