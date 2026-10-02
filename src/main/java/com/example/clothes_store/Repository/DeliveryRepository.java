package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByOrderId(Long orderId);


    Optional<Delivery> findByTrackingNumber(String trackingNumber);
}
