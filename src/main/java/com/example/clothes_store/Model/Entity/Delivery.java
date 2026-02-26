package com.example.clothes_store.Model.Entity;

import com.example.clothes_store.Model.Eum.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String trackingNumber;

    private LocalDateTime shipped_date;
    private LocalDateTime delivered_date;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",unique = true)
    private Order order;
}
