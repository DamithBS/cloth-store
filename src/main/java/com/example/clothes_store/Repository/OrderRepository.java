package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserIdOrderByCreateAtDesc(Long userId);
}
