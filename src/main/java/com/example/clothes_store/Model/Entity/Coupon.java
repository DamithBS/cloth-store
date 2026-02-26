package com.example.clothes_store.Model.Entity;

import com.example.clothes_store.Model.Eum.DiscountType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String couponCode;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @OneToMany(mappedBy = "coupon",cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<CouponUsage> couponUsages;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",unique = true)
    private Order order;
}
