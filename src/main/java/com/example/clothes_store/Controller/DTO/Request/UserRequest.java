package com.example.clothes_store.Controller.DTO.Request;

import com.example.clothes_store.Model.Entity.Address;
import com.example.clothes_store.Model.Eum.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserRequest {

    private String name;

    private String userName;

    private String email;

    private String password;

    private String phone;

    private LocalDateTime createdAt;

    private Role role;

    private List<AddressRequest> addresses = new ArrayList<>();
}
