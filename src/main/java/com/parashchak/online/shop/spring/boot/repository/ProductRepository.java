package com.parashchak.online.shop.spring.boot.repository;

import com.parashchak.online.shop.spring.boot.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    void add(Product product);

    Optional<Product> findById(int id);

    void deleteById(int id);

    void update(int id, Product product);
}