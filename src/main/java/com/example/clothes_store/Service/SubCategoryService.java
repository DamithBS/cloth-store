package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Request.SubCategoryRequest;
import com.example.clothes_store.Exception.SubCategoryNotFoundException;
import com.example.clothes_store.Model.Entity.SubCategory;

import java.util.List;


 //Service interface for managing subcategory-related business logic.
public interface SubCategoryService {

    //Create a new subcategory under a given category.
    void create (Long id, SubCategoryRequest subCategoryRequest);

    // Retrieve all subcategories from the database.
    List<SubCategory> findAll();

    //Find a subcategory by its ID.
    SubCategory findById(Long id) throws SubCategoryNotFoundException;

    //Update an existing subcategory.
    void update(Long id , SubCategoryRequest subCategoryRequest);

    //Delete a subcategory by ID.
    void delete(Long id);
}
