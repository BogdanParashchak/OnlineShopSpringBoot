package com.parashchak.online.shop.spring.boot.web;

import com.parashchak.online.shop.spring.boot.entity.Product;
import com.parashchak.online.shop.spring.boot.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
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
    void givenListOfProducts_whenFindAll_thenActualProductsReturned() throws Exception {

        //prepare
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
    void givenEmptyListOfProducts_whenFindAll_thenEmptyJsonReturned() throws Exception {

        //prepare
        when(productService.findAll()).thenReturn(new ArrayList<>());

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
    void givenListOfProductsToBeFound_whenSearch_thenActualProductsToBeFoundReturned() throws Exception {

        //prepare
        when(productService.search("description")).thenReturn(expectedProducts);

        //then
        mockMvc.perform(get("/products/search/description")
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

        verify(productService).search("description");
    }

    @Test
    void givenSingleProductToBeFound_whenSearch_thenActualSingleProductReturned() throws Exception {

        //prepare
        when(productService.search("second")).thenReturn(List.of(expectedProducts.get(1)));

        //then
        mockMvc.perform(get("/products/search/second")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.[0].id").value(2))
                .andExpect(jsonPath("$.[0].name").value("secondProduct"))
                .andExpect(jsonPath("$.[0].price").value(200))
                .andExpect(jsonPath("$.[0].creationDate").value("-999999999-01-01T00:00:00"))
                .andExpect(jsonPath("$.[0].description").value("secondProductDescription"));

        verify(productService).search("second");
    }

    @Test
    void givenListOfProducts_whenSearchTextDoesNotMatch_thenEmptyJsonReturned() throws Exception {

        //prepare
        when(productService.search("textWhichDoesNotMatch")).thenReturn(new ArrayList<>());

        //then
        mockMvc.perform(get("/products/search/textWhichDoesNotMatch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

        verify(productService).search("textWhichDoesNotMatch");
    }

    @Test
    void whenAdd_thenResponseWithOkStatusSent() throws Exception {

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
    void givenListOfProducts_whenFindById_thenActualProductReturned() throws Exception {

        //prepare
        when(productService.findById(3)).thenReturn(expectedProducts.get(2));

        //then
        mockMvc.perform(get("/product/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("thirdProduct"))
                .andExpect(jsonPath("$.price").value(300))
                .andExpect(jsonPath("$.creationDate").value("2000-01-01T00:00:00"))
                .andExpect(jsonPath("$.description").value("thirdProductDescription"));

        verify(productService).findById(3);
    }

    @Test
    void givenNonExistingId_whenFindById_thenResponseWithNotFoundStatusSent() throws Exception {

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
    void whenDeleteById_thenResponseWithOkStatusSent() throws Exception {

        //prepare
        when(productService.findById(3)).thenReturn(expectedProducts.get(2));

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk());

        verify(productService).deleteById(3);
    }

    @Test
    void givenNonExistingId_whenDeleteById_thenResponseWithNotFoundStatusSent() throws Exception {

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
    void whenUpdate_thenResponseWithOkStatusSent() throws Exception {

        //prepare
        when(productService.findById(1)).thenReturn(expectedProducts.get(0));

        //then
        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                \t"name":"product",
                                \t"price":"200",
                                \t"creationDate":"2000-01-01T00:00:00.000000",
                                \t"description":"productDescription"
                                }"""))

                .andExpect(status().isOk());

        verify(productService).update(eq(1), any(Product.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenResponseWithNotFoundStatusSent() throws Exception {

        //prepare
        doThrow(new IllegalStateException("product with id=5 not found")).when(productService).update(eq(5), any(Product.class));

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

        verify(productService).update(eq(5), any(Product.class));
    }
}