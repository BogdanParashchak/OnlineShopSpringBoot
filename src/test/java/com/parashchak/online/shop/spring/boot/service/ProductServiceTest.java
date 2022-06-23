package com.parashchak.online.shop.spring.boot.service;

import com.parashchak.online.shop.spring.boot.entity.Product;
import com.parashchak.online.shop.spring.boot.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void whenFindAll_thenProductRepositoryFindAllCalled() {
        //when
        productService.findAll();

        //then
        verify(productRepository).findAll();
    }

    @Test
    void givenListOfProducts_whenFindAll_thenListOfTheSameProductsReturned() {
        //prepare
        Product firstProduct = Product.builder()
                .id(1)
                .name("firstProduct")
                .price(100)
                .creationDate(LocalDateTime.MAX)
                .description("firstProductDescription")
                .build();

        Product secondProduct = Product.builder()
                .id(2)
                .name("secondProduct")
                .price(200)
                .creationDate(LocalDateTime.MIN)
                .description("secondProduct")
                .build();

        Product thirdProduct = Product.builder()
                .id(3)
                .name("thirdProduct")
                .price(300)
                .creationDate(LocalDateTime.now())
                .description("thirdProduct")
                .build();

        List<Product> expectedProducts = List.of(firstProduct, secondProduct, thirdProduct);
        Mockito.when(productRepository.findAll()).thenReturn(expectedProducts);

        //when
        List<Product> actualProducts = productService.findAll();

        //then
        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    void givenEmptyListOfProducts_whenFindAll_thenEmptyListReturned() {
        //prepare
        List<Product> emptyProductList = new ArrayList<>();
        Mockito.when(productRepository.findAll()).thenReturn(emptyProductList);

        //when
        List<Product> actualProducts = productService.findAll();

        //then
        assertTrue(actualProducts.isEmpty());
    }

    @Test
    void whenAdd_thenProductRepositorySaveMethodCalled() {
        //prepare
        Product product = Product.builder()
                .name("product")
                .price(500)
                .creationDate(LocalDateTime.MIN)
                .description("productDescription")
                .build();

        //when
        productService.add(product);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        //then
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertEquals(product, capturedProduct);
    }

    @Test
    void whenFindById_thenProductRepositoryFindByIdCalled() {
        //prepare
        Product product = Product.builder()
                .id(5)
                .name("product")
                .price(500)
                .creationDate(LocalDateTime.MIN)
                .description("productDescription")
                .build();

        Mockito.when(productRepository.findById(5)).thenReturn(Optional.of(product));

        //when
        productService.findById(5);

        //then
        verify(productRepository).findById(5);
    }

    @Test
    void givenProduct_whenFindById_thenThisProductReturned() {
        //prepare
        Product product = Product.builder()
                .id(5)
                .name("product")
                .price(500)
                .creationDate(LocalDateTime.MIN)
                .description("productDescription")
                .build();

        Mockito.when(productRepository.findById(5)).thenReturn(Optional.of(product));

        //when
        Product actualProduct = productService.findById(5);

        //then
        assertEquals(5, actualProduct.getId());
        assertEquals("product", actualProduct.getName());
        assertEquals(500, actualProduct.getPrice());
        assertEquals(LocalDateTime.MIN, actualProduct.getCreationDate());
        assertEquals("productDescription", actualProduct.getDescription());
    }

    @Test
    void givenNonExistingId_whenFindById_thenExceptionThrown() {
        //prepare
        Mockito.when(productRepository.findById(5)).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(IllegalStateException.class, () -> productService.deleteById(5));
        String expectedMessage = "product with id=5 not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void whenDeleteById_thenProductRepositoryFindByIdAndDeleteByIdCalled() {
        //prepare
        Product product = Product.builder()
                .id(5)
                .name("product")
                .price(500)
                .creationDate(LocalDateTime.MIN)
                .description("productDescription")
                .build();

        Mockito.when(productRepository.findById(5)).thenReturn(Optional.of(product));

        //when
        productService.deleteById(5);

        //then
        verify(productRepository).findById(5);
        verify(productRepository).deleteById(5);
    }

    @Test
    void givenNonExistingId_whenDeleteById_thenExceptionThrown() {
        //prepare
        Mockito.when(productRepository.findById(5)).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(IllegalStateException.class, () -> productService.deleteById(5));
        String expectedMessage = "product with id=5 not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void givenNonExistingId_whenDeleteById_thenProductRepositoryDeleteByIdNotCalled() {
        //prepare
        Mockito.when(productRepository.findById(5)).thenReturn(Optional.empty());

        //then
        verify(productRepository, never()).deleteById(anyInt());
    }

    @Test
    void whenUpdate_thenProductRepositoryFindByIdAndSaveCalled() {
        //prepare
        Product product = Product.builder()
                .name("product")
                .price(500)
                .description("productDescription")
                .build();

        Mockito.when(productRepository.findById(5)).thenReturn(Optional.of(product));

        //when
        productService.update(5, product);

        //then
        verify(productRepository).findById(5);
        verify(productRepository).save(product);
    }

    @Test
    void givenNonExistingId_whenUpdate_thenExceptionThrown() {
        //prepare
        Mockito.when(productRepository.findById(5)).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(IllegalStateException.class, () -> productService.deleteById(5));
        String expectedMessage = "product with id=5 not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void givenNonExistingId_whenUpdate_thenProductRepositorySaveNotCalled() {
        //prepare
        Mockito.when(productRepository.findById(5)).thenReturn(Optional.empty());

        //then
        verify(productRepository, never()).save(any(Product.class));
    }
}