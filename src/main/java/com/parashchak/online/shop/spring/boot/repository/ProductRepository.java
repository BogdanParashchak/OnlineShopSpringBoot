package com.parashchak.online.shop.spring.boot.repository;

import com.parashchak.online.shop.spring.boot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}