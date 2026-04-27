package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Request.BrandRequest;
import com.example.clothes_store.Exception.BrandNotFoundException;
import com.example.clothes_store.Model.Entity.Brand;

import java.util.List;

//Service interface for managing brand-related business logic.
public interface BrandService {

    //create a new brand
    void create(BrandRequest brandRequest);

    //Retrieve all brand from the database.
    List<Brand> findAll();

    //Find a brand by its ID.
    Brand findById(Long id) throws BrandNotFoundException;

    //Update an existing brand by ID.
    void update(Long id , BrandRequest brandRequest);

    //Delete a brand by ID.
    void delete(Long id);
}
