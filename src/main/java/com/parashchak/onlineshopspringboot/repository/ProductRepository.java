package com.parashchak.onlineshopspringboot.repository;

import com.parashchak.onlineshopspringboot.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();

    void add(Product product);
}