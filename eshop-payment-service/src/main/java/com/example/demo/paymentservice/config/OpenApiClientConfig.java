package com.example.demo.paymentservice.config;
import com.example.demo.payment.client.ApiClient;
import com.example.demo.payment.client.api.DefaultApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenApiClientConfig {

    @Bean
    public ApiClient apiClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080") // замени на нужный URL
                .build();

        return new ApiClient(webClient);
    }

    @Bean
    public DefaultApi defaultApi(ApiClient apiClient) {
        return new DefaultApi(apiClient);
    }
}
