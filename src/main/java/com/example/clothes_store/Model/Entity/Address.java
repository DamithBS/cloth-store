package com.example.clothes_store.Model.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 20)
    private String postal_code;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Boolean is_default;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
