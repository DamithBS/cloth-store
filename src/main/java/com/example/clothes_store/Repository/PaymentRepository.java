package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

    Optional<Payment> findByTransactionId(Long transactionId);
}
