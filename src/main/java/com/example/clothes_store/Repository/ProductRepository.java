package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product ,Long> {

    boolean existsByNameIgnoreCase(String name);

//    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "productVariants",
            "productVariants.inventory",
            "subCategory",
            "subCategory.category",
            "brand",
            "productImages"
    })
    @Query("SELECT p FROM Product p")
//    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "productVariants",
            "productVariants.inventory",
            "subCategory",
            "subCategory.category",
            "brand",
            "productImages"
    })
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findById(Long id);
}
