package com.parashchak.online.shop.spring.boot.repository.jdbc.mapper;

import com.parashchak.online.shop.spring.boot.entity.Product;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductRowMapperTest {

    ResultSet mockResultSet;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("product");
        when(mockResultSet.getDouble("price")).thenReturn(50.00);
        when(mockResultSet.getTimestamp("creation_date")).thenReturn(Timestamp.valueOf("2022-04-13 10:00:00"));
        when(mockResultSet.getString("description")).thenReturn("product_description");

    }

    @Test
    @SneakyThrows
    @DisplayName("get not NULL Product instance from ResultSet row")
    void givenResultSet_whenProductMapperCalled_ThenProductNotNullReturned() {
        //prepare
        ProductRowMapper productRowMapper = new ProductRowMapper();
        //when
        Product actualProduct = productRowMapper.mapRow(mockResultSet, 5);
        //then
        assertNotNull(actualProduct);
    }

    @Test
    @SneakyThrows
    @DisplayName("get Product instance with set fields from ResultSet row")
    void givenResultSet_whenProductMapperCalled_ThenProductWithSetFieldsReturned() {
        //prepare
        ProductRowMapper productRowMapper = new ProductRowMapper();
        //when
        Product actualProduct = productRowMapper.mapRow(mockResultSet, 5);
        //then
        assertEquals(1, actualProduct.getId());
        assertEquals("product", actualProduct.getName());
        assertEquals(50, actualProduct.getPrice());
        assertEquals(LocalDateTime.parse("2022-04-13T10:00:00"), actualProduct.getCreationDate());
        assertEquals("product_description", actualProduct.getDescription());
    }
}