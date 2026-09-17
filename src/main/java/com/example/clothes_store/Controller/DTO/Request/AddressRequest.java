package com.example.clothes_store.Controller.DTO.Request;

import lombok.Data;

@Data
public class AddressRequest {

    private String street;

    private String city;

    private String postal_code;

    private String district;

    private String country;

    private Boolean isDefault;
}
