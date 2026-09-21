package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Optional<Inventory> findByProductVariantId(Long productVariantId);
}
