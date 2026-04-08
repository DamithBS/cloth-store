package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.CategoryRequest;
import com.example.clothes_store.Exception.CategoryAlreadyExistsException;
import com.example.clothes_store.Exception.CategoryNotFoundException;
import com.example.clothes_store.Model.Entity.Category;
import com.example.clothes_store.Repository.CategoryRepository;
import com.example.clothes_store.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    //Creates a new category after checking for duplicate category names.
    @Override
    public void create(CategoryRequest categoryRequest){

        // Remove leading spaces before duplicate check
        String categoryName = categoryRequest.getName().trim();

        // Prevent duplicate category names (case-insensitive)
        if(categoryRepository.existsByNameIgnoreCase(categoryName)){
            throw new CategoryAlreadyExistsException("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        categoryRepository.save(category);
    }


    //Returns all categories from the database.
    @Override
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }


    /* Finds a category by its ID.
       Throws an exception if the category is not found. */
    @Override
    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("Category not found ")
        );
    }

    //Updates an existing category with new name and description.
    @Override
    public void update(Long id, CategoryRequest categoryRequest){
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("Category not found")
        );

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        categoryRepository.save(category);

    }


    //Deletes a category by ID after confirming it exists.
    @Override
    public void delete(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("Category not found")
        );
        categoryRepository.delete(category);
    }

}
