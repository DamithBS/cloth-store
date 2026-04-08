package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Request.CategoryRequest;
import com.example.clothes_store.Exception.CategoryNotFoundException;
import com.example.clothes_store.Model.Entity.Category;

import java.util.List;

//Service interface for managing category-related business operations.
public interface CategoryService {

    //Create a new category.
    void create(CategoryRequest categoryRequest);

    //Retrieve all categories from the database.
    List<Category> findAll();

    //Find a category by its ID.
    Category findById(Long id) throws CategoryNotFoundException;

    //Update an existing category by ID.
    void update(Long id, CategoryRequest categoryRequest);

    //Delete a category by ID.
    void delete(Long id);

}
