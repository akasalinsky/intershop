package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.demo.service.ProductCacheService;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private final ProductCacheService productCacheService;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCacheService productCacheService) {
        this.productRepository = productRepository;
        this.productCacheService = productCacheService;
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> getProductById(Long id) {
        if (id == null) {
            return Mono.empty();
        }

        return productCacheService.getProductFromCache(id)
                .switchIfEmpty(
                        productRepository.findById(id)
                                .flatMap(product ->
                                        productCacheService.saveProductToCache(product)
                                                .thenReturn(product)
                                )
                );
    }

    public Mono<Product> saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Mono<Void> deleteProductById(Long id) {
        return productRepository.deleteById(id);
    }
}