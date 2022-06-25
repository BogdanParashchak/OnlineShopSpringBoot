package com.parashchak.online.shop.spring.boot.service;

import com.parashchak.online.shop.spring.boot.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    void add(Product product);

    Product findById(int id);

    List<Product> search(String text);

    void deleteById(int id);

    void update(int id, Product product);


}
