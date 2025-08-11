// src/test/java/com/example/demo/controller/ProductControllerTest.java
package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class) // Тестируем только ProductController
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc; // Объект для имитации HTTP-запросов

    @MockBean
    private ProductRepository productRepository; // Мокаем репозиторий

    @Test
    public void shouldReturnMainPage() throws Exception {
        // Подготавливаем фиктивные данные
        List<Product> mockProducts = Arrays.asList(
                new Product(1L, "Тестовый товар 1", "Описание 1", new BigDecimal("100.00"), "/images/test1.jpg"),
                new Product(2L, "Тестовый товар 2", "Описание 2", new BigDecimal("200.00"), "/images/test2.jpg")
        );

        // Настраиваем мок, чтобы метод findAll() возвращал наши фиктивные данные
        // Важно: контроллер использует productRepository.findAll(), поэтому мокаем именно его
        when(productRepository.findAll()).thenReturn(mockProducts);

        // Выполняем GET-запрос к корню приложения
        mockMvc.perform(get("/"))
                // Проверяем, что сервер вернул HTTP 200 (OK)
                .andExpect(status().isOk())
                // Проверяем, что был использован шаблон "main"
                .andExpect(view().name("main"))
                // Проверяем, что в модель были добавлены атрибуты
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(model().attributeExists("cartItemQuantity"));
        // Можно добавить больше проверок, например:
        // .andExpect(model().attribute("products", hasSize(2)));
    }
}