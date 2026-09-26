package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {


}
