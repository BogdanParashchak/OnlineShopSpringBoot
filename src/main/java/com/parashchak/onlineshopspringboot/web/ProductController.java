package com.parashchak.onlineshopspringboot.web;

import com.parashchak.onlineshopspringboot.entity.Product;
import com.parashchak.onlineshopspringboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final ProductService productService;

    @GetMapping
    public List<Product> findAll() {
        List<Product> products = productService.findAll();
        logger.info("products {}", products);
        return products;
    }

    @PostMapping
    public void add(@RequestBody Product product) {
        logger.info("add product {}", product);
        productService.add(product);
    }
}