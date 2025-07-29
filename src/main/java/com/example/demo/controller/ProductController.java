package com.example.demo.controller;

import com.example.demo.model.PageInfo;
import com.example.demo.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Controller
public class ProductController {

    private List<Product> products = Arrays.asList(
            new Product(1L, "iPhone", "Смартфон", new BigDecimal("999.99"), "/img/iphone.jpg"),
            new Product(2L, "AirPods", "Наушники", new BigDecimal("199.99"), "/img/airpods.jpg")
    );

    @GetMapping({"/", "/products"})
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        int totalProducts = products.size();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        // Ограничиваем номер страницы
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        // Вычисляем, какие товары показывать
        int start = (page - 1) * size;
        int end = Math.min(start + size, totalProducts);
        List<Product> pageProducts = products.subList(start, end);

        // Создаём объект пагинации
        PageInfo paging = new PageInfo(size, page, totalPages, totalProducts);

        model.addAttribute("products", pageProducts);
        model.addAttribute("paging", paging);

        return "main";
    }
}