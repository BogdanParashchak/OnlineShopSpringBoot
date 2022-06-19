package com.parashchak.onlineshopspringboot.service;

import com.parashchak.onlineshopspringboot.entity.Product;
import com.parashchak.onlineshopspringboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void add(Product product) {
        productRepository.add(product);
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "product with id=" + id + " not found"));
    }

    @Override
    public void deleteById(int id) {
        findById(id);
        productRepository.deleteById(id);
    }

    @Override
    public void update(int id, Product product) {
        findById(id);
        productRepository.update(id, product);
    }
}