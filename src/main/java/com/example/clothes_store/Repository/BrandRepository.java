package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand,Long> {

    boolean existsByNameIgnoreCase(String name);
}
