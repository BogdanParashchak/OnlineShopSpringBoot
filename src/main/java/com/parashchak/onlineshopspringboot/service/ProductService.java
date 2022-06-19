package com.parashchak.onlineshopspringboot.service;

import com.parashchak.onlineshopspringboot.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    void add(Product product);

    Product findById(int id);

    void deleteById(int id);

    void update(int id, Product product);
}
