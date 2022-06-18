package com.parashchak.onlineshopspringboot.service;

import com.parashchak.onlineshopspringboot.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    void add(Product product);
}
