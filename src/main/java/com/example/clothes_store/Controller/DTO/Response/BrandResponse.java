package com.example.clothes_store.Controller.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
}
