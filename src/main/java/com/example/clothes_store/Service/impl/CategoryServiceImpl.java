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

    @Override
    public void create(CategoryRequest categoryRequest){

        String categoryName = categoryRequest.getName().trim();

        if(categoryRepository.existsByNameIgnoreCase(categoryName)){
            throw new CategoryAlreadyExistsException("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        categoryRepository.save(category);
    }


    @Override
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }


    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("Category not found ")
        );
    }

    @Override
    public void update(Long id, CategoryRequest categoryRequest){
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("Category not found")
        );

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        categoryRepository.save(category);

    }

    public void delete(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new CategoryNotFoundException("Category not found")
        );
        categoryRepository.delete(category);
    }

}
