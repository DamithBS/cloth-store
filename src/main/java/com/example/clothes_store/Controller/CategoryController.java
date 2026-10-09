package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Request.CategoryRequest;
import com.example.clothes_store.Controller.DTO.Response.CategoryResponse;
import com.example.clothes_store.Model.Entity.Category;
import com.example.clothes_store.Service.CategoryService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //Creates a new category.
    @RolesAllowed("ADMIN")
    @PostMapping
    public ResponseEntity<String> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest
       ){
        categoryService.create(categoryRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Category created successfully");
    }


    //Retrieves all categories and converts them into response DTOs.
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAllCategory(){

        return ResponseEntity.ok(categoryService.findAll());
    }


    //Retrieves a single category by its ID.
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> categoryFindById(
            @PathVariable("categoryId") Long categoryId
    ){

        return ResponseEntity.ok(categoryService.findById(categoryId));
    }


    //Updates an existing category by ID.
    @RolesAllowed("ADMIN")
    @PutMapping("/{categoryId}")
    public ResponseEntity<String> updateCategory(
            @PathVariable ("categoryId") Long categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ){
        categoryService.update(categoryId,categoryRequest);
        return ResponseEntity.ok("category update successfully");
    }


    //Delete an existing category by ID.
    @RolesAllowed("ADMIN")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable ("categoryId") Long categoryId
    ){
        categoryService.delete(categoryId);
        return ResponseEntity.ok("category delete successfully");
    }
}
