package com.example.clothes_store.Model.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String image_url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
