package com.example.demo.service;

import com.example.demo.config.RedisConfig;
import com.example.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ProductCacheService {

    private final ReactiveRedisTemplate<String, Product> redisTemplate;
    private final long ttlSeconds;

    private static final String PRODUCT_KEY_PREFIX = "product:";

    @Autowired
    public ProductCacheService(ReactiveRedisTemplate<String, Product> redisTemplate, RedisConfig redisConfig) {
        this.redisTemplate = redisTemplate;
        this.ttlSeconds = redisConfig.getProductTtlSeconds();
    }

    public Mono<Product> getProductFromCache(Long id) {
        if (id == null) {
            return Mono.empty();
        }
        String key = PRODUCT_KEY_PREFIX + id;
        return redisTemplate.opsForValue().get(key);
    }

    public Mono<Void> saveProductToCache(Product product) {
        if (product == null || product.getId() == null) {
            return Mono.empty();
        }
        String key = PRODUCT_KEY_PREFIX + product.getId();
        Duration ttl = Duration.ofSeconds(ttlSeconds);
        return redisTemplate.opsForValue().set(key, product, ttl).then();
    }

    public Mono<Void> evictProductFromCache(Long id) {
        if (id == null) {
            return Mono.empty();
        }
        String key = PRODUCT_KEY_PREFIX + id;
        return redisTemplate.delete(key).then();
    }

    public Mono<Void> clearAllProductCache() {
        return Mono.empty();
    }
}