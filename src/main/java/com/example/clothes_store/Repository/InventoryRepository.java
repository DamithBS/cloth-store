package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Optional<Inventory> findByProductVariantId(Long productVariantId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.productVariant.id = :productVariantId
            """)
    Optional<Inventory> findByProductVariantIdForUpdate(
            @Param("productVariantId") Long productVariantId
    );
}
