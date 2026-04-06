package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Request.CategoryRequest;
import com.example.clothes_store.Exception.CategoryNotFoundException;
import com.example.clothes_store.Model.Entity.Category;

import java.util.List;

public interface CategoryService {

    void create(CategoryRequest categoryRequest);

    List<Category> findAll();

    Category findById(Long id) throws CategoryNotFoundException;

    void update(Long id, CategoryRequest categoryRequest);

    void delete(Long id);

}
