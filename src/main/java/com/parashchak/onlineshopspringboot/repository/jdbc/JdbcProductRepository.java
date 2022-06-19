package com.parashchak.onlineshopspringboot.repository.jdbc;

import com.parashchak.onlineshopspringboot.entity.Product;
import com.parashchak.onlineshopspringboot.repository.ProductRepository;
import com.parashchak.onlineshopspringboot.repository.jdbc.mapper.ProductRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcProductRepository implements ProductRepository {

    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();
    private static final String FIND_ALL_PRODUCTS_QUERY = "SELECT id,name, price, creation_date, description " +
            "FROM products ORDER BY id";
    private static final String ADD_PRODUCT_QUERY = "INSERT INTO products (name, price, creation_date, description) " +
            "VALUES (:name, :price, :creationDate, :description)";
    private static final String FIND_PRODUCT_BY_ID_QUERY = "SELECT id, name, price, creation_date, description " +
            "FROM products WHERE id=?";
    private static final String DELETE_PRODUCT_QUERY = "DELETE FROM products WHERE id=?";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE products SET name=?,price=?, description=? WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(FIND_ALL_PRODUCTS_QUERY, PRODUCT_ROW_MAPPER);
    }

    @Override
    public void add(Product product) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", product.getName());
        parameters.put("price", product.getPrice());
        parameters.put("creationDate", product.getCreationDate());
        parameters.put("description", product.getDescription());
        namedParameterJdbcTemplate.update(ADD_PRODUCT_QUERY, parameters);
    }

    @Override
    public Optional<Product> findById(int id) {
        return jdbcTemplate.query(FIND_PRODUCT_BY_ID_QUERY, PRODUCT_ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_PRODUCT_QUERY, id);
    }

    @Override
    public void update(int id, Product product) {
        jdbcTemplate.update(UPDATE_PRODUCT_QUERY, product.getName(), product.getPrice(), product.getDescription(), id);
    }
}