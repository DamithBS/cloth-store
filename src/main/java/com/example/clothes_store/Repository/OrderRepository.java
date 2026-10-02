package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserIdOrderByCreateAtDesc(Long userId);

    Optional<Order> findByIdAndUserUserName(Long Id,String userName);

    @Query("""
        SELECT COUNT(o) > 0
        FROM Order o
        JOIN o.orderItems oi
        JOIN oi.productVariant pv
        JOIN o.delivery d
        WHERE o.user.id = :userId
        AND pv.product.id = :productId
        AND d.deliveryStatus = com.example.clothes_store.Model.Eum.DeliveryStatus.DELIVERED
""")
    boolean existsDeliveredOrderContainingProduct(
            @Param("userId") Long userId,
            @Param("productId") Long productId
    );
}
