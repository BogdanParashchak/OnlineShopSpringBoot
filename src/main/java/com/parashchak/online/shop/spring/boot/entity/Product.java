package com.parashchak.online.shop.spring.boot.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @SequenceGenerator(
            name = "products_id_seq",
            sequenceName = "products_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "products_id_seq"
    )
    private int id;
    private String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;
}