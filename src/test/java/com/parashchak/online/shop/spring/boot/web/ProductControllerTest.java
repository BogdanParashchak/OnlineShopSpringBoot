package com.parashchak.online.shop.spring.boot.web;

import com.parashchak.online.shop.spring.boot.entity.Product;
import com.parashchak.online.shop.spring.boot.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void findAllTest() throws Exception {

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

        List<Product> expectedProducts = List.of(firstProduct, secondProduct, thirdProduct);
        when(productService.findAll()).thenReturn(expectedProducts);

        //then
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("firstProduct"))
                .andExpect(jsonPath("$.[0].price").value(100))
                .andExpect(jsonPath("$.[0].creationDate").value("+999999999-12-31T23:59:59.999999999"))
                .andExpect(jsonPath("$.[0].description").value("firstProductDescription"))

                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("secondProduct"))
                .andExpect(jsonPath("$.[1].price").value(200))
                .andExpect(jsonPath("$.[1].creationDate").value("-999999999-01-01T00:00:00"))
                .andExpect(jsonPath("$.[1].description").value("secondProductDescription"))

                .andExpect(jsonPath("$.[2].id").value(3))
                .andExpect(jsonPath("$.[2].name").value("thirdProduct"))
                .andExpect(jsonPath("$.[2].price").value(300))
                .andExpect(jsonPath("$.[2].creationDate").value("2000-01-01T00:00:00"))
                .andExpect(jsonPath("$.[2].description").value("thirdProductDescription"));

        verify(productService).findAll();
    }

    @Test
    void findAllOnEmptyListTest() throws Exception {

        //prepare
        List<Product> expectedProducts = new ArrayList<>();
        when(productService.findAll()).thenReturn(expectedProducts);

        //then
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

        verify(productService).findAll();
    }

    @Test
    void addTest() throws Exception {

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                \t"name":"firstProduct",
                                \t"price":"1000",
                                \t"creationDate":"2000-01-01T00:00:00.000000",
                                \t"description":"description"
                                }"""))

                .andExpect(status().isOk());

        verify(productService).add(any(Product.class));
    }

    @Test
    void findByIdTest() throws Exception {

        //prepare
        Product product = Product.builder()
                .id(5)
                .name("product")
                .price(100)
                .creationDate(LocalDateTime.of(
                        2000, 1, 1,
                        0, 0, 0, 0))
                .description("productDescription")
                .build();

        when(productService.findById(5)).thenReturn(product);

        //then
        mockMvc.perform(get("/product/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("product"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.creationDate").value("2000-01-01T00:00:00"))
                .andExpect(jsonPath("$.description").value("productDescription"));

        verify(productService).findById(5);
    }

    @Test
    void findByIdThrowsException() throws Exception {

        //prepare
        when(productService.findById(5)).thenThrow(
                new IllegalStateException("product with id=" + 5 + " not found"));

        //then
        mockMvc.perform(get("/product/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("product with id=5 not found"));

        verify(productService).findById(5);
    }

    @Test
    void deleteByIdTest() throws Exception {

        //prepare
        Product product = Product.builder()
                .id(5)
                .name("product")
                .price(100)
                .creationDate(LocalDateTime.of(
                        2000, 1, 1,
                        0, 0, 0, 0))
                .description("productDescription")
                .build();

        when(productService.findById(5)).thenReturn(product);

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk());

        verify(productService).deleteById(5);
    }

    @Test
    void deleteByIdTestThrowsException() throws Exception {

        //prepare
        when(productService.findById(5)).thenThrow(
                new IllegalStateException("product with id=" + 5 + " not found"));

        //then
        mockMvc.perform(get("/product/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("product with id=5 not found"));

        verify(productService).findById(anyInt());
        verify(productService, never()).deleteById(anyInt());
    }

    @Test
    void updateTest() throws Exception {

        //prepare
        Product product = Product.builder()
                .id(5)
                .name("firstProduct")
                .price(100)
                .creationDate(LocalDateTime.of(
                        2000, 1, 1,
                        0, 0, 0, 0))
                .description("productDescription")
                .build();

        when(productService.findById(5)).thenReturn(product);

        //then
        mockMvc.perform(put("/product/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                \t"name":"product",
                                \t"price":"200",
                                \t"creationDate":"2000-01-01T00:00:00.000000",
                                \t"description":"productDescription"
                                }"""))

                .andExpect(status().isOk());

        verify(productService).update(eq(5), any(Product.class));
    }

    @Test
    void updateTestException() throws Exception {

        //prepare
        doThrow(new IllegalStateException("product with id=5 not found")).when(productService).update(anyInt(), any(Product.class));

        //then
        mockMvc.perform(put("/product/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                \t"name":"product",
                                \t"price":"200",
                                \t"creationDate":"2000-01-01T00:00:00.000000",
                                \t"description":"productDescription"
                                }"""))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("product with id=5 not found"));

        verify(productService).update(anyInt(), any(Product.class));
    }
}