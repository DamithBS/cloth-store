package com.example.clothes_store.Model.Entity;

import com.example.clothes_store.Model.Eum.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String transaction_id;

    @Column(nullable = false, length = 100)
    private String gateway;

    @Enumerated(EnumType.STRING)
    private PaymentStatus  paymentStatus;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

}
