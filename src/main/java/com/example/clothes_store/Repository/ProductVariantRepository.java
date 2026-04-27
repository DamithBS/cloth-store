package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {

    boolean existsBySkuIgnoreCase(String sku);
}
