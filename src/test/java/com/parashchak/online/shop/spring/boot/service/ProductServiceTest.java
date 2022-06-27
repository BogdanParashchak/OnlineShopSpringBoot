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

    private List<Product> expectedProducts;

    @BeforeEach
    void setup() {

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
                .description("secondProductDescription")
                .build();

        Product thirdProduct = Product.builder()
                .id(3)
                .name("thirdProduct")
                .price(300)
                .creationDate(LocalDateTime.of(
                        2000, 1, 1,
                        0, 0, 0, 0))
                .description("thirdProductDescription")
                .build();

        expectedProducts = List.of(firstProduct, secondProduct, thirdProduct);
    }

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
        Mockito.when(productRepository.findAll()).thenReturn(expectedProducts);

        //when
        List<Product> actualProducts = productService.findAll();

        //then
        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    void givenEmptyListOfProducts_whenFindAll_thenEmptyListReturned() {

        //prepare
        Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<>());

        //when
        List<Product> actualProducts = productService.findAll();

        //then
        assertTrue(actualProducts.isEmpty());
    }

    @Test
    void whenSearch_thenProductRepositoryAppropriateMethodCalled() {
        //when
        productService.search("text");

        //then
        verify(productRepository)
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("text"
                        , "text");
    }

    @Test
    void givenListOfProductsToBeFound_whenSearch_thenActualProductsToBeFoundReturned() {

        //prepare
        Mockito.when(productRepository
                        .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("description"
                                , "description"))
                .thenReturn(expectedProducts);

        //when
        List<Product> actualProducts = productService.search("description");

        //then
        verify(productRepository)
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("description"
                        , "description");
        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    void givenSingleProductToBeFound_whenSearch_thenActualSingleProductReturned() {

        //prepare
        Product expectedProduct = expectedProducts.get(1);
        when(productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("second"
                        , "second"))
                .thenReturn(List.of(expectedProduct));

        //when
        List<Product> actualProducts = productService.search("second");

        //then
        verify(productRepository)
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("second"
                        , "second");
        assertEquals(expectedProduct, actualProducts.get(0));
    }

    @Test
    void givenListOfProducts_whenSearchTextDoesNotMatch_thenEmptyListReturned() {
        //prepare
        when(productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("textWhichDoesNotMatch"
                        , "textWhichDoesNotMatch"))
                .thenReturn(new ArrayList<>());

        //when
        List<Product> actualProducts = productService.search("textWhichDoesNotMatch");

        //then
        assertTrue(actualProducts.isEmpty());
    }

    @Test
    void whenAdd_thenProductRepositorySaveMethodCalled() {

        //prepare
        Product expectedProduct = expectedProducts.get(1);

        //when
        productService.add(expectedProduct);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        //then
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertEquals(expectedProduct, capturedProduct);
    }

    @Test
    void whenFindById_thenProductRepositoryFindByIdCalled() {

        //prepare
        Mockito.when(productRepository.findById(2)).thenReturn(Optional.of(expectedProducts.get(1)));

        //when
        productService.findById(2);

        //then
        verify(productRepository).findById(2);
    }

    @Test
    void givenProduct_whenFindById_thenThisProductReturned() {
        //prepare
        Mockito.when(productRepository.findById(2)).thenReturn(Optional.of(expectedProducts.get(1)));

        //when
        Product actualProduct = productService.findById(2);

        //then
        assertEquals(2, actualProduct.getId());
        assertEquals("secondProduct", actualProduct.getName());
        assertEquals(200, actualProduct.getPrice());
        assertEquals(LocalDateTime.MIN, actualProduct.getCreationDate());
        assertEquals("secondProductDescription", actualProduct.getDescription());
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
        Mockito.when(productRepository.findById(2)).thenReturn(Optional.of(expectedProducts.get(1)));

        //when
        productService.deleteById(2);

        //then
        verify(productRepository).findById(2);
        verify(productRepository).deleteById(2);
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
        Mockito.when(productRepository.findById(2)).thenReturn(Optional.of(expectedProducts.get(1)));

        //when
        productService.update(2, expectedProducts.get(1));

        //then
        verify(productRepository).findById(2);
        verify(productRepository).save(expectedProducts.get(1));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenExceptionThrown() {

        //prepare
        Mockito.when(productRepository.findById(5)).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(IllegalStateException.class,
                () -> productService.update(5, expectedProducts.get(1)));
        String expectedMessage = "product with id=5 not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void givenNonExistingId_whenUpdate_thenProductRepositorySaveNotCalled() {

        //prepare
        Mockito.when(productRepository.findById(5)).thenReturn(Optional.empty());


        //then
        Exception exception = assertThrows(IllegalStateException.class,
                () -> productService.update(5, expectedProducts.get(1)));
        verify(productRepository, never()).save(any(Product.class));
    }
}