// src/main/java/com/example/demo/repository/OrderRepository.java
package com.example.demo.repository;

import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Дополнительные методы поиска можно добавить позже
}