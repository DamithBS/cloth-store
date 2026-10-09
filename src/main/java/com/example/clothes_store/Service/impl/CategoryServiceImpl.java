package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.CategoryRequest;
import com.example.clothes_store.Controller.DTO.Response.CategoryResponse;
import com.example.clothes_store.Exception.CategoryAlreadyExistsException;
import com.example.clothes_store.Exception.CategoryNotFoundException;
import com.example.clothes_store.Model.Entity.Category;
import com.example.clothes_store.Repository.CategoryRepository;
import com.example.clothes_store.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    //Creates a new category after checking for duplicate category names.
    @Override
    @CacheEvict(value = "categories", key = "'all'")
    public void create(CategoryRequest categoryRequest){

        // Remove leading spaces before duplicate check
        String categoryName = categoryRequest.getName().trim();

        // Prevent duplicate category names (case-insensitive)
        if(categoryRepository.existsByNameIgnoreCase(categoryName)){
            throw new CategoryAlreadyExistsException("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setDescription(categoryRequest.getDescription());

        categoryRepository.save(category);
    }


    //Returns all categories from the database.
    @Override
    @Cacheable(value = "categories", key = "'all'")
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(){

        log.info("========== DATABASE HIT ==========");

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    /* Finds a category by its ID.
       Throws an exception if the category is not found. */
    @Override
    @Cacheable(value = "categories", key = "#id")
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id){

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Category not found")
                );

        return mapToResponse(category);
    }

    //Updates an existing category with new name and description.
    @Override
    @CacheEvict(value = "categories" ,allEntries = true)
    public void update(Long id, CategoryRequest categoryRequest){

        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("Category not found")
        );

        String categoryName = categoryRequest.getName().trim();

        // Check duplicate name
        if (!category.getName().equalsIgnoreCase(categoryName)
                && categoryRepository.existsByNameIgnoreCase(
                categoryName
        )) {
            throw new CategoryAlreadyExistsException(
                    "Category with this name already exists"
            );
        }

        category.setName(categoryName);
        category.setDescription(categoryRequest.getDescription());

        categoryRepository.save(category);

    }


    //Deletes a category by ID after confirming it exists.
    @Override
    @CacheEvict(value = "categories" ,allEntries = true)
    public void delete(Long id){

        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("Category not found")
        );
        categoryRepository.delete(category);
    }


    // ENTITY -> DTO MAPPING
    private CategoryResponse mapToResponse(Category category){
        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());

        return categoryResponse;
    }

}
