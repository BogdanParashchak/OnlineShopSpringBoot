package com.parashchak.online.shop.spring.boot.service;

import com.parashchak.online.shop.spring.boot.entity.Product;
import com.parashchak.online.shop.spring.boot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        productRepository.save(product);
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
        Product foundProduct = findById(id);
        foundProduct.setName(product.getName());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setDescription(product.getDescription());
        productRepository.save(foundProduct);
    }
}