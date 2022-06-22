package com.parashchak.online.shop.spring.boot.repository.jdbc.mapper;

import com.parashchak.online.shop.spring.boot.entity.Product;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDateTime;

@Component
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