package com.example.demo.config;

import com.example.demo.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${app.cache.product.ttl:300}")
    private long productTtlSeconds;

    @Bean
    public ReactiveRedisTemplate<String, Product> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory) {

        StringRedisSerializer keySerializer = new StringRedisSerializer();

        Jackson2JsonRedisSerializer<Product> valueSerializer =
                new Jackson2JsonRedisSerializer<>(Product.class);

        RedisSerializationContext<String, Product> serializationContext =
                RedisSerializationContext.<String, Product>newSerializationContext(keySerializer)
                        .key(keySerializer)
                        .value(valueSerializer)
                        .hashKey(keySerializer)
                        .hashValue(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }

    public long getProductTtlSeconds() {
        return productTtlSeconds;
    }
}