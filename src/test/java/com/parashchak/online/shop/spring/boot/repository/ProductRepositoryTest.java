package com.parashchak.online.shop.spring.boot.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.parashchak.online.shop.spring.boot.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DataSet("products.yml")
    void givenListOfProducts_whenFindAll_thenActualProductsReturned() {
        List<Product> actualProducts = productRepository.findAll();
        assertEquals(3, actualProducts.size());
    }

    @Test
    @DataSet("single_product.yml")
    void givenListOfSingleProduct_whenFindAll_thenActualProductReturned() {
        List<Product> actualProducts = productRepository.findAll();
        assertEquals(1, actualProducts.size());
    }

    @Test
    @DataSet("products.yml")
    void givenEmptyListOfProducts_whenFindAll_thenEmptyListReturned() {

        //prepare
        productRepository.deleteAll();

        //when
        List<Product> actualProducts = productRepository.findAll();

        //then
        assertTrue(actualProducts.isEmpty());
    }

    @Test
    @DataSet("products.yml")
    void givenListOfProductsToBeFound_whenSearch_thenActualProductsToBeFoundReturned() {

        List<Product> products = productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "product", "product");
        assertEquals(3, products.size());
    }

    @Test
    @DataSet("products.yml")
    void givenSingleProductToBeFound_whenSearch_thenActualSingleProductReturned() {

        List<Product> products = productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "secondProduct", "secondProduct");
        assertEquals(1, products.size());
    }

    @Test
    @DataSet("products.yml")
    void givenListOfProducts_whenSearchTextDoesNotMatch_thenEmptyListReturned() {

        List<Product> products = productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "textWhichDoesNotMatch", "textWhichDoesNotMatch");
        assertTrue(products.isEmpty());
    }

    @Test
    @ExpectedDataSet("single_product.yml")
    void whenSave_thenProductSaved() {

        //prepare
        productRepository.deleteAll();

        Product product = Product.builder()
                .name("product")
                .price(500)
                .creationDate(LocalDateTime.of(
                        5000, 1, 1,
                        1, 1, 1))
                .description("productDescription")
                .build();

        //then
        productRepository.save(product);
    }

    @Test
    @DataSet("products.yml")
    void givenProduct_whenFindById_thenThisProductReturned() {

        //when
        Product actualProduct = productRepository.findById(2).get();

        //then
        assertEquals(2, actualProduct.getId());
        assertEquals("secondProduct", actualProduct.getName());
        assertEquals(200, actualProduct.getPrice());
        assertEquals(LocalDateTime.of(
                2000, 1, 1,
                1, 1, 1), actualProduct.getCreationDate());
        assertEquals("secondProductDescription", actualProduct.getDescription());
    }

    @Test
    @DataSet("products.yml")
    void givenNonExistingId_whenFindById_thenEmptyOptionalReturned() {
        Optional<Product> actualProduct = productRepository.findById(5);
        assertTrue(actualProduct.isEmpty());
    }

    @Test
    @DataSet("products.yml")
    @ExpectedDataSet("products_after_delete.yml")
    void whenDeleteById_thenProductDeleted() {
        productRepository.deleteById(2);
    }

    @Test
    @DataSet("products.yml")
    @ExpectedDataSet("products.yml")
    void givenNonExistingId_whenDeleteById_thenExceptionThrown() {
        assertFalse(productRepository.existsById(5));
        assertThrows(Exception.class, () -> productRepository.deleteById(5));
    }
}