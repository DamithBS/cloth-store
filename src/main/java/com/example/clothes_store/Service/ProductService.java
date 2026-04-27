package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Request.ProductRequest;
import com.example.clothes_store.Exception.ProductNotFoundException;
import com.example.clothes_store.Model.Entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    void create(ProductRequest productRequest);

    List<Product> findAll(Pageable pageable);

    Product findById(Long id) throws ProductNotFoundException;

    void update(Long id, ProductRequest productRequest);

    void delete(Long id);
}
