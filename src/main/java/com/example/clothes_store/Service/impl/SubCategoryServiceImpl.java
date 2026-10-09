package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.SubCategoryRequest;
import com.example.clothes_store.Controller.DTO.Response.SubCategoryResponse;
import com.example.clothes_store.Exception.CategoryNotFoundException;
import com.example.clothes_store.Exception.SubCategoryNotFoundException;
import com.example.clothes_store.Model.Entity.Category;
import com.example.clothes_store.Model.Entity.SubCategory;
import com.example.clothes_store.Repository.CategoryRepository;
import com.example.clothes_store.Repository.SubCategoryRepository;
import com.example.clothes_store.Service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

     /* Creates a new subcategory and links it to an existing category.
        Throws an exception if the parent category does not exist.    */
    @Override
    @CacheEvict(value = "subCategories" ,allEntries = true)
    public void create(Long id, SubCategoryRequest subCategoryRequest){

        //Find the parent category first, because every subcategory must belong to a category
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("category not found..")
        );

        //Create and populate the new subcategory entity
        SubCategory subCategory = new SubCategory();
        subCategory.setName(subCategoryRequest.getName());

        // Set the relationship between subcategory and category
        subCategory.setCategory(category);

        // Save the subcategory to the database
        subCategoryRepository.save(subCategory);
    }

    //Returns all subcategories stored in the database.
    @Override
    @Cacheable(value = "subCategories" , key = "'all'")
    @Transactional(readOnly = true)
    public List<SubCategoryResponse> findAll(){

        return subCategoryRepository.findAll()
                .stream()
                .map(this:: mapToResponse)
                .toList();

    }

    /* Finds a subcategory by ID.
       Throws an exception if it does not exist. */
    @Override
    @Cacheable(value ="subCategories" , key = "#id")
    @Transactional(readOnly = true)
    public SubCategoryResponse findById(Long id){

        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(()-> new SubCategoryNotFoundException("sub category not found"));

        return mapToResponse(subCategory);
    }

    //Updates the name of an existing subcategory.
    @Override
    @CacheEvict(value = "subCategories", allEntries = true)
    public void update(Long id, SubCategoryRequest subCategoryRequest){

        //Fetch the existing subcategory first
        SubCategory subCategory =  subCategoryRepository.findById(id).orElseThrow(
                ()->new SubCategoryNotFoundException("sub category not found..")
        );

        String subCategoryName= subCategoryRequest.getName().trim();

        if (!subCategory.getName().equalsIgnoreCase(subCategoryName)
                && subCategoryRepository.existsByNameIgnoreCase(subCategoryName))
        {
            throw new SubCategoryNotFoundException("sub category name already exists");
        }

            //Update only the editable fields
            subCategory.setName(subCategoryName);

            //Save updated entity
            subCategoryRepository.save(subCategory);

    }

    //Deletes a subcategory by ID.
    @Override
    @CacheEvict(value = "subCategories", allEntries = true)
    public void delete(Long id){

        //Ensure the subcategory exists before attempting deletion
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                ()-> new SubCategoryNotFoundException("Sub Category not found..")
        );

        // Delete the entity
        subCategoryRepository.delete(subCategory);
    }


    //Helper method to convert SubCategory entity into SubCategoryResponse DTO.
    private SubCategoryResponse mapToResponse(SubCategory subCategory){

        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();

        subCategoryResponse.setName(subCategory.getName());
        subCategoryResponse.setId(subCategory.getId());
        subCategoryResponse.setCategoryName(subCategory.getCategory().getName());
        subCategoryResponse.setCategoryDescriptions(subCategory.getCategory().getDescription());

        return subCategoryResponse;
    }
}
