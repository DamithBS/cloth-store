package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Request.BrandRequest;
import com.example.clothes_store.Controller.DTO.Response.BrandResponse;
import com.example.clothes_store.Model.Entity.Brand;
import com.example.clothes_store.Service.BrandService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    //Create a new brand
    @RolesAllowed("ADMIN")
    @PostMapping
    public ResponseEntity<String> createBrand(
            @Valid @RequestBody BrandRequest brandRequest
    ){
        brandService.create(brandRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Brand created successfully");
    }

    //Retrieves all brand and converts them into response DTOs.
    @GetMapping
    public ResponseEntity<List<BrandResponse>> findAllBrand(){

        return ResponseEntity.ok(brandService.findAll());
    }


    //Retrieves a single brand by its ID.
    @GetMapping("/{brandId}")
    public ResponseEntity<BrandResponse> brandFindById(
            @PathVariable("brandId") Long id
    ){

        return ResponseEntity.ok(brandService.findById(id));
    }


    //Updates an existing brand by ID.
    @RolesAllowed("ADMIN")
    @PutMapping("/{brandId}")
    public ResponseEntity<String> updateBrand(
            @PathVariable("brandId") Long id,
            @Valid @RequestBody BrandRequest brandRequest
    ){
        brandService.update(id,brandRequest);
        return ResponseEntity.ok("brand update successfully");
    }


    //Delete an existing Brand by ID.
    @RolesAllowed("ADMIN")
    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> deleteBrand(
            @PathVariable("brandId") Long id
    ){
        brandService.delete(id);
        return ResponseEntity.ok("brand delete successfully");
    }
}
