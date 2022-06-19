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
        logger.info("found products: {}", products);
        return products;
    }

    @PostMapping
    public void add(@RequestBody Product product) {
        productService.add(product);
        logger.info("added product: {}", product);
    }

    @GetMapping(path = "/{id}")
    public Product findById(@PathVariable int id) {
        Product product = productService.findById(id);
        logger.info("found by id={} product: {}", id, product);
        return product;
    }

    @DeleteMapping(path = "/{id}")
    public void deleteById(@PathVariable int id) {
        productService.deleteById(id);
        logger.info("deleted product by id={}", id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") int id,
                       @RequestBody Product product) {
        productService.update(id, product);
        logger.info("updated by id={} product: {}", id, product);
    }
}