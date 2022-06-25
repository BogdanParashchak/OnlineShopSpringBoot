package com.parashchak.online.shop.spring.boot.web;

import com.parashchak.online.shop.spring.boot.service.ProductService;
import com.parashchak.online.shop.spring.boot.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<Product> findAll() {
        List<Product> products = productService.findAll();
        log.info("found products: {}", products);
        return products;
    }

    @PostMapping("/products")
    public void add(@RequestBody Product product) {
        productService.add(product);
        log.info("added product: {}", product);
    }

    @GetMapping(path = "product/{id}")
    public Product findById(@PathVariable int id) {
        Product product = productService.findById(id);
        log.info("found by id={} product: {}", id, product);
        return product;
    }

    @GetMapping("/products/search/{text}")
    public List<Product>  searchByText(@PathVariable String text) {
        List<Product> products = productService.search(text);
        log.info("found products: {}", products);
        return products;
    }

    @DeleteMapping(path = "product/{id}")
    public void deleteById(@PathVariable int id) {
        productService.deleteById(id);
        log.info("deleted product by id={}", id);
    }

    @PutMapping("product/{id}")
    public void update(@PathVariable int id,
                       @RequestBody Product product) {
        productService.update(id, product);
        log.info("updated by id={} product: {}", id, product);
    }
}