package com.parashchak.onlineshopspringboot.repository.jdbc.mapper;

import com.parashchak.onlineshopspringboot.entity.Product;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.time.LocalDateTime;

public class ProductRowMapper implements RowMapper<Product> {

    @SneakyThrows
    public Product mapRow(ResultSet resultSet, int numRow) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        LocalDateTime creationDate = resultSet.getTimestamp("creation_date").toLocalDateTime();
        String description = resultSet.getString("description");
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .creationDate(creationDate)
                .description(description)
                .build();
    }
}